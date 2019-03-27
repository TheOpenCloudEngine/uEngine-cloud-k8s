package com.example.template.k8s.deployment;

import com.example.template.k8s.user.TempUser;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

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
        deploymentRepository.deleteAllInQuery();
        return "";
    }

    public String deleteByHost(String host) {
        deploymentRepository.deleteByHost(host);
        return "";
    }

    public void delete(String host, String namespace, String name) {
        deploymentRepository.deleteDeploy(host, namespace, name);
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
        Map<String,String> userData =  TempUser.getUserDetail(null);

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
        Map<String,String> userData =  TempUser.getUserDetail(null);

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
        Map<String,String> userData =  TempUser.getUserDetail(null);

        JSONObject data = new JSONObject();
        data.put("host", userData.get("host"));
        data.put("token", userData.get("token"));
        data.put("namespace", namespace);
        data.put("name", name);
        data.put("type", "DEPLOY");
        data.put("command", "DELETE");
        kafkaTemplate.send(new ProducerRecord<String, JSONObject>(orderTopic, namespace , data));
    }



}
