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
package nl.uva.sne.drip.drip.provisioner;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.uva.sne.drip.commons.types.MessageParameter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * This is an example of a Message consumer
 *
 *
 * @author H. Zhou, S. Koulouzis
 */
public class Consumer extends DefaultConsumer {

    private final Channel channel;
    private final String propertiesPath = "etc/consumer.properties";

    Map<String, String> em = new HashMap<>();

//    private String jarFilePath;
    public class TopologyElement {

        String topologyName = "";
        String outputFilePath = "";
    }

    public Consumer(Channel channel) throws IOException {
        super(channel);
        this.channel = channel;
        Properties prop = new Properties();
        try (InputStream in = new FileInputStream(propertiesPath)) {
            prop.load(in);
        }
//        jarFilePath = prop.getProperty("jar.file.path", "/root/SWITCH/bin/ProvisioningCore.jar");
//        File jarFile = new File(jarFilePath);
//        if (!jarFile.exists()) {
//            throw new IOException(jarFile.getAbsolutePath() + " not found!");
//        } else {
//            jarFilePath = jarFile.getAbsolutePath();
//        }

        em.put("Virginia", "ec2.us-east-1.amazonaws.com");
        em.put("California", "ec2.us-west-1.amazonaws.com");
        em.put("Oregon", "ec2.us-west-2.amazonaws.com");
        em.put("Mumbai", "ec2.ap-south-1.amazonaws.com");
        em.put("Singapore", "ec2.ap-southeast-1.amazonaws.com");
        em.put("Seoul", "ec2.ap-northeast-2.amazonaws.com");
        em.put("Sydney", "ec2.ap-southeast-2.amazonaws.com");
        em.put("Tokyo", "ec2.ap-northeast-1.amazonaws.com");
        em.put("Frankfurt", "ec2.eu-central-1.amazonaws.com");
        em.put("Ireland", "ec2.eu-west-1.amazonaws.com");
        em.put("Paulo", "ec2.sa-east-1.amazonaws.com");
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

            ArrayList topologyInfoArray;
            topologyInfoArray = invokeProvisioner(message, tempInputDirPath);
            response = generateResponse(topologyInfoArray);

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

    ////If the provisioner jar file is successfully invoked, the returned value should be a set of output file paths which are expected.
    ////If there are some errors or some information missing with this message, the returned value will be null.
    ////The input dir path contains '/'
    private ArrayList<TopologyElement> invokeProvisioner(String message, String tempInputDirPath) throws IOException, JSONException {
        //Use the Jackson API to convert json to Object 
        JSONObject jo = new JSONObject(message);
        JSONArray parameters = jo.getJSONArray("parameters");

        //Create tmp input files 
        File ec2ConfFile = null;
        File geniConfFile = null;
        //loop through the parameters in a message to find the input files
        String logDir, mainTopologyPath, sshKeyFilePath, scriptPath;
        ArrayList<TopologyElement> topologyInfoArray = new ArrayList();
        List<String> certificateNames = new ArrayList();

        File cloudConfFile = getCloudConfigurationFile(parameters, tempInputDirPath);
        if (cloudConfFile.getName().toLowerCase().contains("ec2")) {
            ec2ConfFile = cloudConfFile;
        } else if (cloudConfFile.getName().toLowerCase().contains("geni")) {
            geniConfFile = cloudConfFile;
        }

        File topologyFile = getTopology(parameters, tempInputDirPath, 0);
        File mainTopologyFile = new File(tempInputDirPath + "topology_main");
        FileUtils.moveFile(topologyFile, mainTopologyFile);
        mainTopologyPath = mainTopologyFile.getAbsolutePath();

        topologyFile = getTopology(parameters, tempInputDirPath, 1);
        File secondaryTopologyFile = new File(tempInputDirPath + File.separator + topologyFile.getName() + ".yml");
        String outputFilePath = tempInputDirPath + File.separator + topologyFile.getName() + "_provisioned.yml";
        TopologyElement x = new TopologyElement();
        x.topologyName = topologyFile.getName();
        x.outputFilePath = outputFilePath;
        topologyInfoArray.add(x);
        FileUtils.moveFile(topologyFile, secondaryTopologyFile);

        Map<String, File> certificatesMap = getCertificates(parameters, tempInputDirPath);
        certificateNames.addAll(certificatesMap.keySet());

        logDir = getLogDirPath(parameters, tempInputDirPath);

        File sshKey = getSSHKey(parameters, tempInputDirPath);
        sshKeyFilePath = sshKey.getAbsolutePath();

        File scriptFile = getSciptFile(parameters, tempInputDirPath);
        scriptPath = scriptFile.getAbsolutePath();

        File curDir = new File(tempInputDirPath);
        for (File f : curDir.listFiles()) {
            String fileType = FilenameUtils.getExtension(f.getName());
            if (fileType != null) {
                if (fileType.equals("yml")) {
                    String toscaFile = f.getAbsolutePath();
                    if (sshKeyFilePath != null) {
                        changeKeyFilePath(toscaFile, sshKeyFilePath);
                    }
                    if (scriptPath != null) {
                        changeGUIScriptFilePath(toscaFile, scriptPath);
                    }
                }
            }
        }

        if (ec2ConfFile == null && geniConfFile == null) {
            return null;
        }
        if (mainTopologyPath == null) {
            return null;
        }
        String ec2ConfFilePath = null;
        String geniConfFilePath = null;
        if (ec2ConfFile != null) {
            ec2ConfFilePath = ec2ConfFile.getAbsolutePath();
            Properties prop = new Properties();
            prop.load(new FileInputStream(ec2ConfFile));
            StringBuilder supportDomains = new StringBuilder();
            String prefix = "";
            for (String certName : certificateNames) {
                String supported = this.em.get(certName);
                if (supported != null) {
                    supportDomains.append(prefix);
                    prefix = ", ";
                    supportDomains.append(supported);
                }
            }
            prop.setProperty("KeyDir", tempInputDirPath);
            prop.setProperty("SupportDomains", supportDomains.toString());
            prop.store(new FileOutputStream(ec2ConfFile), null);

        }
        if (geniConfFile != null) {
            geniConfFilePath = geniConfFile.getAbsolutePath();
            Properties prop = new Properties();
            prop.load(new FileInputStream(geniConfFile));
            prop.propertyNames();
            prop.setProperty("KeyDir", tempInputDirPath);
            prop.store(new FileOutputStream(geniConfFile), null);
        }

        String cmd = "ec2=" + ec2ConfFilePath + " exogeni=" + geniConfFilePath + " logDir=" + logDir + " topology=" + mainTopologyPath;
        Provisioning.ProvisioningCore.main(cmd.split(" "));
//        String cmd = "java -jar " + jarFilePath + " ec2=" + ec2ConfFilePath + " exogeni=" + geniConfFilePath + " logDir=" + logDir + " topology=" + mainTopologyPath;
//        try {
//            Logger.getLogger(Consumer.class.getName()).log(Level.INFO, "Executing: " + cmd);
//            Process p = Runtime.getRuntime().exec(cmd);
//            p.waitFor();
//        } catch (IOException | InterruptedException e) {
//            // TODO Auto-generated catch block
//            Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, e);
//        }

        x = new TopologyElement();
        x.topologyName = "kubernetes";
        x.outputFilePath = tempInputDirPath + "file_kubernetes";
        topologyInfoArray.add(x);

        return topologyInfoArray;
    }

    ////Change the key file path in the tosca file. 
    ////Because the user needs to upload their public key file into the server file system. 
    private void changeKeyFilePath(String toscaFilePath, String newKeyFilePath) {
        File toscaFile = new File(toscaFilePath);
        String fileContent = "";
        try (BufferedReader in = new BufferedReader(new FileReader(toscaFile))) {
            String line;
            while ((line = in.readLine()) != null) {
                if (line.contains("publicKeyPath")) {
                    fileContent += ("publicKeyPath: " + newKeyFilePath + "\n");
                } else {
                    fileContent += (line + "\n");
                }
            }
            try (FileWriter fw = new FileWriter(toscaFilePath, false)) {
                fw.write(fileContent);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, e);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    private void changeGUIScriptFilePath(String toscaFilePath, String newScriptPath) {
        File toscaFile = new File(toscaFilePath);
        String fileContent = "";
        try (BufferedReader in = new BufferedReader(new FileReader(toscaFile))) {
            String line;
            while ((line = in.readLine()) != null) {
                if (line.contains("script")) {
                    int index = line.indexOf("script:");
                    String prefix = line.substring(0, index + 7);
                    fileContent += (prefix + " " + newScriptPath + "\n");
                } else {
                    fileContent += (line + "\n");
                }
            }
            try (FileWriter fw = new FileWriter(toscaFilePath, false)) {
                fw.write(fileContent);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, e);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private String generateResponse(ArrayList<TopologyElement> outputs) throws JSONException, IOException {
        //Use the JSONObject API to convert Object (Message) to json
        JSONObject jo = new JSONObject();
        jo.put("creationDate", (System.currentTimeMillis()));
        List parameters = new ArrayList();
        String charset = "UTF-8";
        if (outputs == null) {
            Map<String, String> fileArguments = new HashMap<>();
            fileArguments.put("encoding", charset);
            fileArguments.put("name", "ERROR");
            fileArguments.put("value", "Some error with input messages!");
            parameters.add(fileArguments);
        } else {
            for (int i = 0; i < outputs.size(); i++) {
                Map<String, String> fileArguments = new HashMap<>();
                fileArguments.put("encoding", charset);
                File f = new File(outputs.get(i).outputFilePath);
                if (f.exists()) {
                    fileArguments.put("name", outputs.get(i).topologyName);
                    byte[] bytes = Files.readAllBytes(Paths.get(f.getAbsolutePath()));
                    fileArguments.put("value", new String(bytes, charset));
                    parameters.add(fileArguments);
                } else {
                    fileArguments.put("name", outputs.get(i).topologyName);
                    fileArguments.put("value", "ERROR::There is no output for topology " + outputs.get(i).topologyName);
                    parameters.add(fileArguments);
                }

            }
        }
        jo.put("parameters", parameters);
        return jo.toString();
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

    private void writeValueToFile(String value, File file) throws FileNotFoundException {
        try (PrintWriter out = new PrintWriter(file)) {
            out.print(value);
        }
        if (!file.exists() || file.length() < value.getBytes().length) {
            throw new FileNotFoundException("File " + file.getAbsolutePath() + " doesn't exist or contents are missing ");
        }
    }

    private File getCloudConfigurationFile(JSONArray parameters, String tempInputDirPath) throws JSONException {
        for (int i = 0; i < parameters.length(); i++) {
            JSONObject param = (JSONObject) parameters.get(i);
            String name = (String) param.get(MessageParameter.NAME);
            if (name.equals("ec2.conf") || name.equals("geni.conf")) {
                try {
                    File confFile = new File(tempInputDirPath + File.separator + name);
                    if (confFile.createNewFile()) {
                        writeValueToFile((String) param.get(MessageParameter.VALUE), confFile);
                        return confFile;
                    } else {
                        return null;
                    }
                } catch (IOException e) {
                    Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, e);
                    return null;
                }
            }
        }
        return null;
    }

    private File getTopology(JSONArray parameters, String tempInputDirPath, int level) throws JSONException, IOException {
        for (int i = 0; i < parameters.length(); i++) {
            JSONObject param = (JSONObject) parameters.get(i);
            String name = (String) param.get(MessageParameter.NAME);
            if (name.equals("topology")) {
                JSONObject attributes = param.getJSONObject("attributes");
                int fileLevel = Integer.valueOf((String) attributes.get("level"));
                if (fileLevel == level) {
                    String originalFilename = (String) attributes.get("filename");
                    String fileName = "";
//                    String[] parts = originalFilename.split("_");
//                    String prefix = "";
//                    //Clear date part form file name
//                    if (isNumeric(parts[0])) {
//                        for (int j = 1; j < parts.length; j++) {
//                            fileName += prefix + parts[j];
//                            prefix = "_";
//                        }
//                    } else {
                    fileName = originalFilename;
//                    }

                    File topologyFile = new File(tempInputDirPath + File.separator + fileName);
                    topologyFile.createNewFile();
                    String val = (String) param.get(MessageParameter.VALUE);
                    writeValueToFile(val, topologyFile);
                    return topologyFile;
                }
            }
        }
        return null;
    }

    private Map<String, File> getCertificates(JSONArray parameters, String tempInputDirPath) throws JSONException, IOException {
        Map<String, File> files = new HashMap<>();
        for (int i = 0; i < parameters.length(); i++) {
            JSONObject param = (JSONObject) parameters.get(i);
            String name = (String) param.get(MessageParameter.NAME);
            if (name.equals("certificate")) {
                JSONObject attribute = param.getJSONObject("attributes");
                String fileName = (String) attribute.get("filename");
                File certificate = new File(tempInputDirPath + File.separator + fileName + ".pem");
                if (certificate.createNewFile()) {
                    writeValueToFile((String) param.get(MessageParameter.VALUE), certificate);
                    files.put(fileName, certificate);
                }
            }
        }
        return files;
    }

    private String getLogDirPath(JSONArray parameters, String tempInputDirPath) throws JSONException {
        for (int i = 0; i < parameters.length(); i++) {
            JSONObject param = (JSONObject) parameters.get(i);
            String name = (String) param.get(MessageParameter.NAME);
            if (name.equals("logdir")) {
                return (String) param.get(MessageParameter.VALUE);
            }
        }
        return System.getProperty("java.io.tmpdir");
    }

    private File getSSHKey(JSONArray parameters, String tempInputDirPath) throws JSONException, IOException {
        for (int i = 0; i < parameters.length(); i++) {
            JSONObject param = (JSONObject) parameters.get(i);
            String name = (String) param.get(MessageParameter.NAME);
            if (name.equals("sshkey")) {
                String sshKeyContent = (String) param.get(MessageParameter.VALUE);
                File sshKeyFile = new File(tempInputDirPath + File.separator + "user.pem");
                if (sshKeyFile.createNewFile()) {
                    writeValueToFile(sshKeyContent, sshKeyFile);
                    return sshKeyFile;
                }
            }
        }
        return null;
    }

    private File getSciptFile(JSONArray parameters, String tempInputDirPath) throws JSONException, IOException {
        for (int i = 0; i < parameters.length(); i++) {
            JSONObject param = (JSONObject) parameters.get(i);
            String name = (String) param.get(MessageParameter.NAME);
            if (name.equals("guiscript")) {
                String scriptContent = (String) param.get(MessageParameter.VALUE);
                File scriptFile = new File(tempInputDirPath + File.separator + "guiscipt.sh");
                if (scriptFile.createNewFile()) {
                    writeValueToFile(scriptContent, scriptFile);
                    return scriptFile;
                }
            }
        }
        return null;
    }

    private static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

}
