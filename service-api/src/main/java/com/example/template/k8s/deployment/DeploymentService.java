package com.example.template.k8s.deployment;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class DeploymentService {

    private static final Logger LOG = LoggerFactory.getLogger(DeploymentService.class);

    @Autowired
    DeploymentRepository deploymentRepository;

    @Value("${topic.orderTopic}")
    private String orderTopic;

    @Autowired
    KafkaTemplate kafkaTemplate;

//    @Cacheable(value="deployment" ,key="#deployment.id")
    public Deployment checkInstance(Deployment deployment){
        return deploymentRepository.findById(deployment.getId()).orElse(new Deployment());
    }

//    @Cacheable(value="deployment")
    public Iterable<Deployment> getAllDeployment(){
        return deploymentRepository.findAll();
    }

//    @Cacheable(value="deployment", key="#provider")
    public Iterable<Deployment> getAllDeploymentByProvider(String provider){
        return deploymentRepository.findByProvider(provider);
    }

//    @Cacheable(value="deployment", key="#provider+#name")
    public Iterable<Deployment> getDeploymentByProviderAndName(String provider, String name){
        return deploymentRepository.findByProviderAndName(provider,name);
    }

//    @Cacheable(value="deployment", key="#namespace")
    public Iterable<Deployment> getDeploymentByNamespace(String namespace){
        return deploymentRepository.findByNamespace(namespace);
    }

//    @Cacheable(value="deployment", key="#namespace+#name")
    public Iterable<Deployment> getDeploymentByNamespaceAndName(String namespace, String name){
        return deploymentRepository.findByNamespaceAndName(namespace, name);
    }

    public String delete() {
        deploymentRepository.deleteAll();
        return "";
    }

    public String update(Deployment deployment) {
    	deploymentRepository.save(deployment);
        return "";
    }

    /**
     * 관련된 캐쉬를 지운다,
     */
//    @Caching(evict = {
//            @CacheEvict(value = "instance"),
//            @CacheEvict(value = "instance", key="#instance.id"),
//            @CacheEvict(value = "instance", key ="#instance.provider"),
//            @CacheEvict(value = "instance", key ="#instance.provider+#instance.name")
//    })
//    public String deleteCacheList(InstanceModel instance) {
//        return "";
//    }

    public void createDeploy(String namespace, String yamlString){

        Yaml yaml = new Yaml();
        Map<String,Object> body = yaml.load(yamlString);

        // TODO
        Map<String,String> userData =  getUserDetail(null);

        JSONObject data = new JSONObject();
        data.put("host", userData.get("host"));
        data.put("token", userData.get("token"));
        data.put("namespace", namespace);
//        data.put("name", name);
        data.put("type", "DEPLOY");
        data.put("command", "CREATE");
        data.put("body", body);
        kafkaTemplate.send(new ProducerRecord<String, JSONObject>(orderTopic, namespace , data));
    }
    public void updateDeploy(String namespace, String name, String yamlString){

        Yaml yaml = new Yaml();
        Map<String,Object> body = yaml.load(yamlString);

        // TODO
        Map<String,String> userData =  getUserDetail(null);

        JSONObject data = new JSONObject();
        data.put("host", userData.get("host"));
        data.put("token", userData.get("token"));
        data.put("namespace", namespace);
        data.put("name", name);
        data.put("type", "DEPLOY");
        data.put("command", "UPDATE");
        data.put("body", body);
        kafkaTemplate.send(new ProducerRecord<String, JSONObject>(orderTopic, namespace , data));
    }

    public void deleteDeploy(String namespace, String name){
        Map<String,String> userData =  getUserDetail(null);

        JSONObject data = new JSONObject();
        data.put("host", userData.get("host"));
        data.put("token", userData.get("token"));
        data.put("namespace", namespace);
        data.put("name", name);
        data.put("type", "DEPLOY");
        data.put("command", "DELETE");
        kafkaTemplate.send(new ProducerRecord<String, JSONObject>(orderTopic, namespace , data));
    }

    public Map<String,String> getUserDetail(String userName){

        // TODO DB 조회
        String host = "https://api.k8s.bzdvops.com";
        String token = "eyJhbGciOiJSUzI1NiIsImtpZCI6IiJ9.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJkZWZhdWx0Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZWNyZXQubmFtZSI6ImNsb3VkdXNlci10b2tlbi16cmtqaiIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50Lm5hbWUiOiJjbG91ZHVzZXIiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC51aWQiOiI0ZmJmNzk0YS0zNTgwLTExZTktYTU2OC0wMjkxMGMyMWIzOTgiLCJzdWIiOiJzeXN0ZW06c2VydmljZWFjY291bnQ6ZGVmYXVsdDpjbG91ZHVzZXIifQ.APncfC7biCYEre4LZ3S-TVcf641qpQlo7r_BN0khN8ovnT7rR3DWGTaUDLP2eFQBLUvEVSAgTT1g0wF1OFsqEx-Sn3fHByyf1r8t15wvN_XJFM2_V_ZZBosUeZCciklcky0jwF6AWcSpUo9nKa23yBtylJ9d6EPjAq8KtURdX7IVb5i8j0InSExyOQZv5xJ-yv55GB_yRrI9rQ6cwxt_PdFaQiFLjSjnp6SvZj3nPACw_qb98w2I4p_O8DZ5SE-b4OetZj0xmZM7ELXBbceMDepT0UHrU1ZcIc54aWNnhyGFdxspxwrGWSDtNL4T6KKbTqU6HVXkiJTKCw1w9E9hHg";

        Map<String,String> returnData = new HashMap<String,String>();
        returnData.put("host", host);
        returnData.put("token", token);

        return returnData;

    }


}
