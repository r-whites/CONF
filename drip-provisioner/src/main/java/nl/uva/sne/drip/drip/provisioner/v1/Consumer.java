/*
 * Copyright 2017 S. Koulouzis, Wang Junchao, Huan Zhou, Yang Hu 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.uva.sne.drip.drip.provisioner.v1;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.KeyPair;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.uva.sne.drip.drip.commons.data.internal.Message;
import nl.uva.sne.drip.drip.commons.data.internal.MessageParameter;
import nl.uva.sne.drip.drip.provisioner.utils.MessageParsing;
import nl.uva.sne.drip.drip.provisioner.utils.PropertyValues;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import provisioning.credential.Credential;
import provisioning.credential.EC2Credential;
import provisioning.credential.EGICredential;
import provisioning.credential.SSHKeyPair;
import provisioning.credential.UserCredential;
import provisioning.database.EC2.EC2Database;
import provisioning.database.EGI.EGIDatabase;
import provisioning.database.UserDatabase;
import provisioning.engine.TEngine.TEngine;
import topologyAnalysis.TopologyAnalysisMain;
import topologyAnalysis.dataStructure.SubTopologyInfo;
import topologyAnalysis.dataStructure.VM;

/**
 *
 * This is a provision Message consumer
 *
 *
 * @author H. Zhou, S. Koulouzis
 */
public class Consumer extends DefaultConsumer {

    private final Channel channel;
//    Map<String, String> em = new HashMap<>();

    public Consumer(Channel channel) throws IOException {
        super(channel);
        this.channel = channel;
//        em.put("Virginia", "ec2.us-east-1.amazonaws.com");
//        em.put("California", "ec2.us-west-1.amazonaws.com");
//        em.put("Oregon", "ec2.us-west-2.amazonaws.com");
//        em.put("Mumbai", "ec2.ap-south-1.amazonaws.com");
//        em.put("Singapore", "ec2.ap-southeast-1.amazonaws.com");
//        em.put("Seoul", "ec2.ap-northeast-2.amazonaws.com");
//        em.put("Sydney", "ec2.ap-southeast-2.amazonaws.com");
//        em.put("Tokyo", "ec2.ap-northeast-1.amazonaws.com");
//        em.put("Frankfurt", "ec2.eu-central-1.amazonaws.com");
//        em.put("Ireland", "ec2.eu-west-1.amazonaws.com");
//        em.put("Paulo", "ec2.sa-east-1.amazonaws.com");
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        //Create the reply properties which tells us where to reply, and which id to use.
        //No need to change anything here 
        AMQP.BasicProperties replyProps = new AMQP.BasicProperties.Builder()
                .correlationId(properties.getCorrelationId())
                .build();

        String response = "";

        try {
            //The queue only moves bytes so we need to convert them to stting 
            String message = new String(body, "UTF-8");

            String tempInputDirPath = System.getProperty("java.io.tmpdir") + File.separator + "Input-" + Long.toString(System.nanoTime()) + File.separator;
            File tempInputDir = new File(tempInputDirPath);
            if (!(tempInputDir.mkdirs())) {
                throw new FileNotFoundException("Could not create input directory: " + tempInputDir.getAbsolutePath());
            }

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            response = mapper.writeValueAsString(invokeProvisioner(message, tempInputDirPath));

        } catch (Throwable ex) {
            try {
                response = generateExeptionResponse(ex);
                Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JSONException ex1) {
                response = "{\"creationDate\": " + System.currentTimeMillis()
                        + ",\"parameters\": [{\"url\": null,\"encoding\": UTF-8,"
                        + "\"value\": \"" + ex.getMessage() + "\",\"name\": \""
                        + ex.getClass().getName() + "\",\"attributes\": null}]}";
            }
        } finally {
            Logger.getLogger(Consumer.class.getName()).log(Level.INFO, "Sending Response: {0}", response);
            //We send the response back. No need to change anything here 
            channel.basicPublish("", properties.getReplyTo(), replyProps, response.getBytes("UTF-8"));
            channel.basicAck(envelope.getDeliveryTag(), false);
        }

    }

    private Message invokeProvisioner(String messageStr, String tempInputDirPath) throws IOException, JSONException, Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        Message massage = mapper.readValue(messageStr, Message.class);
        List<MessageParameter> params = massage.getParameters();
        for (MessageParameter mp : params) {
            String name = mp.getName();
            String value = mp.getValue();
            if (name.equals("action")) {
                switch (value) {
                    case "start_topology":
                        JSONObject jo = new JSONObject(messageStr);
                        return startTopology(jo.getJSONArray("parameters"), tempInputDirPath);
                    case "kill_topology":
                        jo = new JSONObject(messageStr);
                        return killTopology(jo.getJSONArray("parameters"), tempInputDirPath);
                }
            }
        }
        return null;
    }

    private Message startTopology(JSONArray parameters, String tempInputDirPath) throws Exception {
        TEngine tEngine = new TEngine();
        TopologyAnalysisMain tam = null;
        UserCredential userCredential = new UserCredential();
        UserDatabase userDatabase = new UserDatabase();
        try {
            File topologyFile = MessageParsing.getTopologies(parameters, tempInputDirPath, 0).get(0);
            File mainTopologyFile = new File(tempInputDirPath + "topology_main.yml");
            FileUtils.moveFile(topologyFile, mainTopologyFile);
            String topTopologyLoadingPath = mainTopologyFile.getAbsolutePath();

            List<File> topologyFiles = MessageParsing.getTopologies(parameters, tempInputDirPath, 1);
            for (File lowLevelTopologyFile : topologyFiles) {
                File secondaryTopologyFile = new File(tempInputDirPath + File.separator + lowLevelTopologyFile.getName() + ".yml");
                FileUtils.moveFile(lowLevelTopologyFile, secondaryTopologyFile);
            }

            Map<String, Object> map = MessageParsing.ymlStream2Map(new FileInputStream(topTopologyLoadingPath));
            String userPublicKeyName = ((String) map.get("publicKeyPath")).split("@")[1].replaceAll("\"", "");
            String userPrivateName = FilenameUtils.removeExtension(userPublicKeyName);
            List<File> sshKeys = MessageParsing.getSSHKeys(parameters, tempInputDirPath, userPublicKeyName, "user_ssh_key");
            if (sshKeys == null || sshKeys.isEmpty()) {
                JSch jsch = new JSch();
                KeyPair kpair = KeyPair.genKeyPair(jsch, KeyPair.RSA);
                kpair.writePrivateKey(tempInputDirPath + File.separator + userPrivateName);
                kpair.writePublicKey(tempInputDirPath + File.separator + userPublicKeyName, "auto generated user accees keys");
                kpair.dispose();
            }

            tam = new TopologyAnalysisMain(topTopologyLoadingPath);
            if (!tam.fullLoadWholeTopology()) {
                throw new Exception("sth wrong!");
            }

            List<Credential> credentials = MessageParsing.getCloudCredentials(parameters, tempInputDirPath);
            for (Credential cred : credentials) {
                ////Initial credentials and ssh key pairs
                if (userCredential.cloudAccess == null) {
                    userCredential.cloudAccess = new HashMap<>();
                }
                if (cred instanceof EC2Credential) {
                    userCredential.cloudAccess.put("ec2", cred);
                }
                if (cred instanceof EGICredential) {
                    userCredential.cloudAccess.put("egi", cred);
                }
            }

            ArrayList<SSHKeyPair> sshKeyPairs = userCredential.
                    loadSSHKeyPairFromFile(tempInputDirPath);
            if (sshKeyPairs == null) {
                throw new NullPointerException("ssh key pairs are null");
            }
            if (sshKeyPairs.isEmpty()) {
                throw new IOException("No ssh key pair is loaded!");
            } else if (!userCredential.initial(sshKeyPairs, tam.wholeTopology)) {
                throw new IOException("ssh key pair initilaziation error");
            }

            ///Initial Database
            EGIDatabase egiDatabase = new EGIDatabase();
            egiDatabase.loadDomainInfoFromFile(PropertyValues.DOMAIN_INFO_PATH + File.separator + "EGI_Domain_Info");
            EC2Database ec2Database = new EC2Database();
            ec2Database.loadDomainFromFile(PropertyValues.DOMAIN_INFO_PATH + File.separator + "domains");
            ec2Database.loadAmiFromFile(PropertyValues.DOMAIN_INFO_PATH + File.separator + "OS_Domain_AMI");
            if (userDatabase.databases == null) {
                userDatabase.databases = new HashMap<>();
            }
            userDatabase.databases.put("ec2", ec2Database);
//        userDatabase.databases.put("egi", egiDatabase);

            /*ProvisionRequest pq = new ProvisionRequest();
		pq.topologyName = "ec2_zh_b";
		ArrayList<ProvisionRequest> provisionReqs = new ArrayList<ProvisionRequest>();
		provisionReqs.add(pq);*/
            //tEngine.provision(tam.wholeTopology, userCredential, userDatabase, provisionReqs);
            tEngine.provisionAll(tam.wholeTopology, userCredential, userDatabase);
            String topologyUserName = tam.wholeTopology.userName;

            String charset = "UTF-8";
            List<MessageParameter> responseParameters = new ArrayList<>();
            MessageParameter param = new MessageParameter();
            param.setEncoding(charset);
            String fileName = tam.wholeTopology.loadingPath;
            if (!fileName.endsWith(".yml")) {
                fileName += ".yml";
            }
            File f = new File(fileName);
            if (f.exists()) {
                param.setName(FilenameUtils.removeExtension(FilenameUtils.getBaseName(fileName)));
                byte[] bytes = Files.readAllBytes(Paths.get(f.getAbsolutePath()));
                param.setValue(new String(bytes, charset));
                responseParameters.add(param);
            } else {
                param.setName(FilenameUtils.removeExtension(FilenameUtils.getBaseName(fileName)));
                param.setValue("ERROR::There is no output for topology topology_main");
                responseParameters.add(param);
            }

            for (SubTopologyInfo sub : tam.wholeTopology.topologies) {
                param = new MessageParameter();
                param.setEncoding(charset);
                fileName = tempInputDirPath + File.separator + sub.topology;
                if (!fileName.endsWith(".yml")) {
                    fileName += ".yml";
                }
                f = new File(fileName);
                if (f.exists()) {
                    param.setName(sub.topology);
                    byte[] bytes = Files.readAllBytes(Paths.get(f.getAbsolutePath()));
                    param.setValue(new String(bytes, charset));
                    responseParameters.add(param);
                } else {
                    param.setName(sub.topology);
                    param.setValue("ERROR::There is no output for topology " + sub.topology);
                    responseParameters.add(param);
                }
            }
            param = new MessageParameter();
            param.setEncoding(charset);
            param.setName("public_user_key");
            byte[] bytes = Files.readAllBytes(Paths.get(tempInputDirPath + File.separator + userPublicKeyName));
            param.setValue(new String(bytes, charset));
            Map<String, String> attributes = new HashMap<>();
            attributes.put("name", userPublicKeyName);
            param.setAttributes(attributes);
            responseParameters.add(param);

            param = new MessageParameter();
            param.setEncoding(charset);
            param.setName("private_user_key");
            bytes = Files.readAllBytes(Paths.get(tempInputDirPath + File.separator + userPrivateName));
            param.setValue(new String(bytes, charset));
            attributes = new HashMap<>();
            attributes.put("name", userPublicKeyName);
//            attributes.put("username",  sub.userName);
            param.setAttributes(attributes);
            responseParameters.add(param);

            param = new MessageParameter();
            param.setEncoding(charset);
            param.setName("private_deployer_key");
            bytes = Files.readAllBytes(Paths.get(tempInputDirPath + File.separator + "clusterKeyPair" + File.separator + "id_rsa"));
            param.setValue(new String(bytes, charset));
            attributes = new HashMap<>();
            attributes.put("name", "id_rsa");
            param.setAttributes(attributes);
            responseParameters.add(param);

            param = new MessageParameter();
            param.setEncoding(charset);
            param.setName("public_deployer_key");
            bytes = Files.readAllBytes(Paths.get(tempInputDirPath + File.separator + "clusterKeyPair" + File.separator + "id_rsa.pub"));
            param.setValue(new String(bytes, charset));
            attributes = new HashMap<>();
            attributes.put("name", "id_rsa.pub");
            param.setAttributes(attributes);
            responseParameters.add(param);

            File dir = new File(tempInputDirPath);
            for (File d : dir.listFiles()) {
                if (d.isDirectory() && !d.getName().equals("clusterKeyPair")) {
                    param = new MessageParameter();
                    param.setEncoding(charset);
                    param.setName("public_cloud_key");
                    bytes = Files.readAllBytes(Paths.get(d.getAbsolutePath() + File.separator + "name.pub"));
                    param.setValue(new String(bytes, charset));
                    attributes = new HashMap<>();
                    attributes.put("name", "name.pub");
                    attributes.put("key_pair_id", d.getName());
                    param.setAttributes(attributes);
                    responseParameters.add(param);

                    param = new MessageParameter();
                    param.setEncoding(charset);
                    param.setName("private_cloud_key");
                    bytes = Files.readAllBytes(Paths.get(d.getAbsolutePath() + File.separator + "id_rsa"));
                    param.setValue(new String(bytes, charset));
                    attributes = new HashMap<>();
                    attributes.put("name", "id_rsa");
                    attributes.put("key_pair_id", d.getName());
                    param.setAttributes(attributes);
                    responseParameters.add(param);
                }

            }

            param = new MessageParameter();
            param.setEncoding(charset);
            param.setName("deploy_parameters");
            String paramValue = "";
            for (SubTopologyInfo sub : tam.wholeTopology.topologies) {
                ArrayList<VM> vms = sub.subTopology.getVMsinSubClass();
                for (VM vm : vms) {
                    if (vm != null) {
                        paramValue += vm.publicAddress + " ";
                        paramValue += sub.userName + " ";
//                        paramValue += tempInputDirPath + File.separator + sub.subTopology.accessKeyPair.SSHKeyPairId + File.separator + "id_rsa";
                        paramValue += vm.role + "\n";
                    }
                }
//            String accessKeyPath = tempInputDirPath + File.separator + sub.subTopology.accessKeyPair.SSHKeyPairId + File.separator + "id_rsa";
//            System.err.println("accessKeyPath: " + accessKeyPath);
            }
            param.setValue(paramValue);
            responseParameters.add(param);

            Message response = new Message();
            response.setCreationDate(System.currentTimeMillis());
            response.setParameters(responseParameters);

            return response;
        } finally {
            if (tam != null) {
                tEngine.deleteAll(tam.wholeTopology, userCredential, userDatabase);
            }
        }
    }

    private String generateExeptionResponse(Throwable ex) throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("creationDate", (System.currentTimeMillis()));
        List parameters = new ArrayList();
        String charset = "UTF-8";

        Map<String, String> errorArgument = new HashMap<>();
        errorArgument.put("encoding", charset);
        errorArgument.put("name", ex.getClass().getName());
        errorArgument.put("value", ex.getMessage());
        parameters.add(errorArgument);

        jo.put("parameters", parameters);
        return jo.toString();
    }

    private Message killTopology(JSONArray parameters, String tempInputDirPath) throws Exception {
        TEngine tEngine = new TEngine();
        TopologyAnalysisMain tam = null;
        UserCredential userCredential = new UserCredential();
        UserDatabase userDatabase = new UserDatabase();

        File topologyFile = MessageParsing.getTopologies(parameters, tempInputDirPath, 0).get(0);
        File mainTopologyFile = new File(tempInputDirPath + "topology_main.yml");
        FileUtils.moveFile(topologyFile, mainTopologyFile);
        String topTopologyLoadingPath = mainTopologyFile.getAbsolutePath();

        List<File> topologyFiles = MessageParsing.getTopologies(parameters, tempInputDirPath, 1);
        for (File lowLevelTopologyFile : topologyFiles) {
            File secondaryTopologyFile = new File(tempInputDirPath + File.separator + lowLevelTopologyFile.getName() + ".yml");
            FileUtils.moveFile(lowLevelTopologyFile, secondaryTopologyFile);
        }
        
          File clusterKeyPair = MessageParsing.getClusterKeysPair(parameters, tempInputDirPath);
          

        tam = new TopologyAnalysisMain(topTopologyLoadingPath);
        if (!tam.fullLoadWholeTopology()) {
            throw new Exception("sth wrong!");
        }

      

        tEngine.deleteAll(tam.wholeTopology, userCredential, userDatabase);
        Message response = new Message();
        return response;
    }

}
