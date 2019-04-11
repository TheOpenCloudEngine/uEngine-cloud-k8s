package com.example.template.manager;

import com.squareup.okhttp.Call;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.ApiResponse;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.V1DeleteOptions;
import io.kubernetes.client.models.V1Pod;
import io.kubernetes.client.models.V1Service;
import io.kubernetes.client.models.V1Status;
import io.kubernetes.client.util.Yaml;
import org.json.simple.JSONObject;

import java.util.Map;

public class PodManager  extends KubeManager {
    public PodManager(String host, String token) {
        super(host, token);
    }

    @Override
    public void create(JSONObject jsonObj) throws ApiException{
        Map<String,Object> data = this.settingInitValue(jsonObj);
        Yaml yaml = new Yaml();
        V1Pod body = yaml.loadAs((String)data.get("body"), V1Pod.class);
        CoreV1Api apiInstance = new CoreV1Api(client);

        V1Pod result = apiInstance.createNamespacedPod((String)data.get("namespace"), body, null, null, null);

    }

    @Override
    public void update(JSONObject jsonObj) throws ApiException{
        Map<String,Object> data = this.settingInitValue(jsonObj);
        String name = (String) jsonObj.get("name");
        Yaml yaml = new Yaml();
        V1Pod body = yaml.loadAs((String)data.get("body"), V1Pod.class);
        CoreV1Api apiInstance = new CoreV1Api(client);

        V1Pod result = apiInstance.replaceNamespacedPod(name, (String)data.get("namespace"), body, null, null);
    }

    @Override
    public void delete(JSONObject jsonObj){
        String name = (String) jsonObj.get("name");
        String namespace = (String) jsonObj.get("namespace");
        CoreV1Api apiInstance = new CoreV1Api(client);
        try {
            V1DeleteOptions v1DeleteOption = new V1DeleteOptions();
            // TODO 삭제는 되지만.. 현재 무조건 에러가 떨어짐 그리하여 이 뒤쪽 로직이 안돌아감
            V1Status result = apiInstance.deleteNamespacedPod(name, namespace, v1DeleteOption, null,null, null,null,null);
        } catch (ApiException e) {
//            e.printStackTrace();
        }
    }
}
