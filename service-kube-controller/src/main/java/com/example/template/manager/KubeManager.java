package com.example.template.manager;

import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.util.Config;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class KubeManager {
    private static final Logger LOG = LoggerFactory.getLogger(KubeManager.class);
    ApiClient client;

    public KubeManager(String host, String token){
        this.client = Config.fromToken(host, token, false);
//        this.client.setDebugging(true);
    }

    public Map<String, Object> settingInitValue(JSONObject jsonObj){
        Map<String, Object> data = new HashMap<String, Object>();
        String namespace = (String) jsonObj.get("namespace");
        if( namespace == null ){
            namespace = "default";
        }
        data.put("namespace", namespace);

        if( jsonObj.containsKey("body") && jsonObj.get("body") != null ){
            Object body = (Object) jsonObj.get("body");
            String bodyTmp = body.toString();
            bodyTmp = bodyTmp.replaceAll("\\\\", "");
            LOG.info("message bodyTmp='{}'" , bodyTmp);
            data.put("body", bodyTmp);
        }


        return data;
    }

    public void create(JSONObject jsonObj) throws ApiException{

    }
    public void update(JSONObject jsonObj) throws ApiException {

    }
    public void delete(JSONObject jsonObj){

    }
}
