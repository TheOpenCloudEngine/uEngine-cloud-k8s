package com.example.template.k8s.service;

import java.util.HashMap;
import java.util.Map;

import com.example.template.k8s.common.TempUser;
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

@Service
public class ServicesService {

    private static final Logger LOG = LoggerFactory.getLogger(ServicesService.class);

    @Autowired
    ServicesRepository servicesRepository;

    @Value("${topic.orderTopic}")
    private String orderTopic;

    @Autowired
    KafkaTemplate kafkaTemplate;


//    @Cacheable(value="service" ,key="#deployment.id")
    public Services checkInstance(Services svs){
        return servicesRepository.findById(svs.getId()).orElse(new Services());
    }

//    @Cacheable(value="service")
    public Iterable<Services> getAllServices(){
        return servicesRepository.findAll();
    }

//    @Cacheable(value="service", key="#provider")
    public Iterable<Services> getServicesByProvider(String provider){
        return servicesRepository.findByProvider(provider);
    }

//    @Cacheable(value="service", key="#provider+#name")
    public Iterable<Services> getServicesByProviderAndName(String provider, String name){
        return servicesRepository.findByProviderAndName(provider,name);
    }

//    @Cacheable(value="service", key="#namespace")
    public Iterable<Services> getServicesByNamespace(String namespace){
        return servicesRepository.findByNamespace(namespace);
    }

//    @Cacheable(value="service", key="#namespace+#name")
    public Iterable<Services> getServicesByNamespaceAndName(String namespace, String name){
        return servicesRepository.findByNamespaceAndName(namespace, name);
    }

    public String delete() {
        servicesRepository.deleteAllInQuery();
        return "";
    }

    public String update(Services svs) {
    	servicesRepository.save(svs);
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



    public void createService(String namespace, String yamlString){

        Yaml yaml = new Yaml();
        Map<String,Object> body = yaml.load(yamlString);

        // TODO
        Map<String,String> userData =  TempUser.getUserDetail(null);

        JSONObject data = new JSONObject();
        data.put("host", userData.get("host"));
        data.put("token", userData.get("token"));
        data.put("namespace", namespace);
        data.put("type", "SERVICE");
        data.put("command", "CREATE");
        data.put("body", body);
        kafkaTemplate.send(new ProducerRecord<String, JSONObject>(orderTopic, namespace , data));
    }

    public void updateService(String namespace, String name, String yamlString){

        Yaml yaml = new Yaml();
        Map<String,Object> body = yaml.load(yamlString);

        // TODO
        Map<String,String> userData =  TempUser.getUserDetail(null);

        JSONObject data = new JSONObject();
        data.put("host", userData.get("host"));
        data.put("token", userData.get("token"));
        data.put("namespace", namespace);
        data.put("name", name);
        data.put("type", "SERVICE");
        data.put("command", "UPDATE");
        data.put("body", body);
        kafkaTemplate.send(new ProducerRecord<String, JSONObject>(orderTopic, namespace , data));
    }

    public void deleteService(String namespace, String name){
        Map<String,String> userData =  TempUser.getUserDetail(null);

        JSONObject data = new JSONObject();
        data.put("host", userData.get("host"));
        data.put("token", userData.get("token"));
        data.put("namespace", namespace);
        data.put("name", name);
        data.put("type", "POD");
        data.put("command", "DELETE");
        kafkaTemplate.send(new ProducerRecord<String, JSONObject>(orderTopic, namespace , data));
    }

}
