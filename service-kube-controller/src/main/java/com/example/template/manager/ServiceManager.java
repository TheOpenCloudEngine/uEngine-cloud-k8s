package com.example.template.manager;

import io.kubernetes.client.ApiException;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.V1DeleteOptions;
import io.kubernetes.client.models.V1Service;
import io.kubernetes.client.models.V1Status;
import io.kubernetes.client.util.Yaml;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ServiceManager extends KubeManager{

    private static final Logger LOG = LoggerFactory.getLogger(ServiceManager.class);

    public ServiceManager(String host, String token) {
        super(host, token);
    }

    @Override
    public void create(JSONObject jsonObj) throws ApiException{
        Map<String,Object> data = this.settingInitValue(jsonObj);
        Yaml yaml = new Yaml();
        V1Service body = yaml.loadAs((String)data.get("body"), V1Service.class);
        CoreV1Api apiInstance = new CoreV1Api(client);

        V1Service result = apiInstance.createNamespacedService((String)data.get("namespace"), body, null, null, null);
    }

    @Override
    public void update(JSONObject jsonObj) throws ApiException {
        Map<String,Object> data = this.settingInitValue(jsonObj);
        String name = (String) jsonObj.get("name");
        Yaml yaml = new Yaml();
        V1Service body = yaml.loadAs((String)data.get("body"), V1Service.class);
        CoreV1Api apiInstance = new CoreV1Api(client);

        V1Service result = apiInstance.replaceNamespacedService(name, (String)data.get("namespace"), body, null, null);
    }

    @Override
    public void delete(JSONObject jsonObj){
        String name = (String) jsonObj.get("name");
        String namespace = (String) jsonObj.get("namespace");
        CoreV1Api apiInstance = new CoreV1Api(client);
        try {
            V1DeleteOptions v1DeleteOption = new V1DeleteOptions();
            V1Status result = apiInstance.deleteNamespacedService(name, namespace, v1DeleteOption, null,null, null,null,null);
        } catch (ApiException e) {
//            e.printStackTrace();
        }
    }
}
