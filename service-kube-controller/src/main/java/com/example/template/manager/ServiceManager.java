package com.example.template.manager;

import io.kubernetes.client.ApiException;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.V1Service;
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
    public void create(JSONObject jsonObj){
        Map<String,Object> data = this.settingInitValue(jsonObj);
        try {
            Yaml yaml = new Yaml();
            V1Service body = yaml.loadAs((String)data.get("body"), V1Service.class);
            CoreV1Api apiInstance = new CoreV1Api(client);

            V1Service result = apiInstance.createNamespacedService((String)data.get("namespace"), body, null, null, null);
            System.out.println(result);

        }catch (ApiException e){
            e.printStackTrace();
        }
    }
}
