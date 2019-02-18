package com.example.template.manager;

import io.kubernetes.client.ApiException;
import io.kubernetes.client.apis.AppsV1Api;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.V1DeleteOptions;
import io.kubernetes.client.models.V1Deployment;
import io.kubernetes.client.models.V1Service;
import io.kubernetes.client.models.V1Status;
import io.kubernetes.client.util.Yaml;
import org.json.simple.JSONObject;

import java.util.Map;

public class DeployManager extends KubeManager {
    public DeployManager(String host, String token) {
        super(host, token);
    }

    @Override
    public void create(JSONObject jsonObj){
        Map<String,Object> data = this.settingInitValue(jsonObj);
        try {
            Yaml yaml = new Yaml();
            V1Deployment body = yaml.loadAs((String)data.get("body"), V1Deployment.class);
            AppsV1Api apiInstance = new AppsV1Api(client);

            V1Deployment result = apiInstance.createNamespacedDeployment((String)data.get("namespace"), body, null, null, null);

        }catch (ApiException e){
            e.printStackTrace();
        }
    }

    @Override
    public void update(JSONObject jsonObj){
        Map<String,Object> data = this.settingInitValue(jsonObj);
        String name = (String) jsonObj.get("name");
        try {
            Yaml yaml = new Yaml();
            V1Deployment body = yaml.loadAs((String)data.get("body"), V1Deployment.class);
            AppsV1Api apiInstance = new AppsV1Api(client);

            V1Deployment result = apiInstance.replaceNamespacedDeployment(name , (String)data.get("namespace"), body, null, null);

        }catch (ApiException e){
            e.printStackTrace();
        }
    }

    @Override
    public void delete(JSONObject jsonObj){
        String name = (String) jsonObj.get("name");
        String namespace = (String) jsonObj.get("namespace");
        AppsV1Api apiInstance = new AppsV1Api(client);
        try {
            V1DeleteOptions v1DeleteOption = new V1DeleteOptions();
            V1Status result = apiInstance.deleteNamespacedDeployment(name, namespace, v1DeleteOption, null,null, null,null,null);
        } catch (ApiException e) {
//            e.printStackTrace();
        }
    }
}
