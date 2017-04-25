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
package nl.uva.sne.drip.api.v0.rest;

import java.io.IOException;
import nl.uva.sne.drip.data.v1.external.ProvisionRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import nl.uva.sne.drip.commons.utils.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import nl.uva.sne.drip.api.service.CloudCredentialsService;
import nl.uva.sne.drip.api.service.PlannerService;
import nl.uva.sne.drip.api.service.ProvisionService;
import nl.uva.sne.drip.api.service.KeyPairService;
import nl.uva.sne.drip.api.service.ScriptService;
import nl.uva.sne.drip.api.service.UserService;
import nl.uva.sne.drip.data.v0.external.Execute;
import nl.uva.sne.drip.data.v0.external.Attribute;
import nl.uva.sne.drip.data.v0.external.Result;
import nl.uva.sne.drip.data.v0.external.Upload;
import nl.uva.sne.drip.data.v1.external.CloudCredentials;
import nl.uva.sne.drip.data.v1.external.KeyPair;
import nl.uva.sne.drip.data.v1.external.ProvisionResponse;
import org.json.JSONException;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * This controller is responsible for obtaining resources from cloud providers
 * based the plan generated by the planner and uploaded by the user
 *
 * @author S. Koulouzis
 */
@RestController
@RequestMapping("/user/v0.0/switch/provision")
@Component
@PreAuthorize("isAuthenticated()")
public class ProvisionController0 {

    @Autowired
    private ScriptService userScriptService;

    @Autowired
    private KeyPairService userKeysService;

    @Autowired
    private CloudCredentialsService cloudCredentialsService;

    @Autowired
    private ProvisionService provisionService;

    @Autowired
    private PlannerService planService;

    @RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = MediaType.TEXT_XML_VALUE)
    @RolesAllowed({UserService.USER, UserService.ADMIN})
    public @ResponseBody
    String provision(@RequestBody Upload upload) {

        ProvisionResponse resp = new ProvisionResponse();
        CloudCredentials cloudCred = cloudCredentialsService.findAll().get(0);
        String cloudCredID = cloudCred.getId();
        List<String> idList = new ArrayList<>();
        idList.add(cloudCredID);
        resp.setCloudCredentialsIDs(idList);
        List<nl.uva.sne.drip.data.v0.external.Attribute> plans = upload.file;
        nl.uva.sne.drip.data.v1.external.PlanResponse topLevelPlan = null;
        Set<String> loweLevelPlansIDs = new HashSet<>();

        for (nl.uva.sne.drip.data.v0.external.Attribute p : plans) {
            nl.uva.sne.drip.data.v1.external.PlanResponse plan1 = Converter.File2Plan1(p);
            if (plan1.getLevel() == 0) {
                topLevelPlan = plan1;
            } else {
                plan1 = planService.save(plan1);
                loweLevelPlansIDs.add(plan1.getId());
            }
        }
        topLevelPlan.setLoweLevelPlansIDs(loweLevelPlansIDs);
        topLevelPlan = planService.save(topLevelPlan);
        String planID = topLevelPlan.getId();
        resp.setPlanID(planID);
        List<KeyPair> allKeys = userKeysService.findAll();
        List<String> keyPairIDs = new ArrayList<>();
        if (allKeys != null && !allKeys.isEmpty()) {
            for (KeyPair keyPair : allKeys) {
                String userKeyID = keyPair.getId();
                keyPairIDs.add(userKeyID);
            }
            resp.setKeyPairIDs(keyPairIDs);
        }
        resp = provisionService.save(resp);
        return "Success: Infrastructure files are uploaded! Action number: "
                + resp.getId();
    }

    @RequestMapping(value = "/execute", method = RequestMethod.POST, consumes = MediaType.TEXT_XML_VALUE, produces = MediaType.TEXT_XML_VALUE)
    @RolesAllowed({UserService.USER, UserService.ADMIN})
    public @ResponseBody
    Result execute(@RequestBody Execute exc) {

        try {
            ProvisionRequest req = provisionService.findOne(exc.action);
            req = provisionService.provisionResources(req);
            Map<String, Object> map = req.getKeyValue();
            String yaml = Converter.map2YmlString(map);
            yaml = yaml.replaceAll("\n", "\\\\n");

            Result res = new Result();
            List<Attribute> files = new ArrayList<>();
            Attribute e = new Attribute();
            e.content = yaml;
            e.level = "0";
            e.name = "provisioned_" + exc.action;
            files.add(e);
            res.info = "INFO";
            res.status = "Success";
            res.file = files;
            return res;
        } catch (IOException | TimeoutException | JSONException | InterruptedException ex) {
            Logger.getLogger(ProvisionController0.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
