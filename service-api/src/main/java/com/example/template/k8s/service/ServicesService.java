package com.example.template.k8s.service;

import java.util.Map;

import com.example.template.k8s.user.UserDetail;
import com.example.template.k8s.user.UserDetailService;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

@Service
public class ServicesService {

    private static final Logger LOG = LoggerFactory.getLogger(ServicesService.class);

    @Autowired
    ServicesRepository servicesRepository;

    @Autowired
    UserDetailService userDetailService;

    @Value("${topic.orderTopic}")
    private String orderTopic;

    @Autowired
    KafkaTemplate kafkaTemplate;


    public Services checkInstance(Services svs){
        return servicesRepository.findById(svs.getId()).orElse(new Services());
    }

    public Iterable<Services> getAllServices(UserDetail userDetail){
        userDetail = userDetailService.getUserDetail(userDetail.getUsername());
        return servicesRepository.findByHost(userDetail.getHost());
    }

    public Iterable<Services> getServicesByNamespace(UserDetail userDetail, String namespace){
        userDetail = userDetailService.getUserDetail(userDetail.getUsername());
        return servicesRepository.findByHostAndNamespace(userDetail.getHost(), namespace);
    }

    public Iterable<Services> getServicesByNamespaceAndName(UserDetail userDetail, String namespace, String name){
        userDetail = userDetailService.getUserDetail(userDetail.getUsername());
        return servicesRepository.findByHostAndNamespaceAndName(userDetail.getHost(), namespace, name);
    }

    public String delete() {
        servicesRepository.deleteAllInQuery();
        return "";
    }

    public String deleteByHost(String host) {
        servicesRepository.deleteByHost(host);
        return "";
    }

    public void delete(String host, String namespace, String name) {
        servicesRepository.deleteService(host, namespace, name);
    }


    public String update(Services svs) {
    	servicesRepository.save(svs);
        return "";
    }


    public void createService(UserDetail userDetail, String namespace, String yamlString){

        Yaml yaml = new Yaml();
        Map<String,Object> body = yaml.load(yamlString);

        userDetail = userDetailService.getUserDetail(userDetail.getUsername());
        if( userDetail.getHost() != null && userDetail.getToken() != null ) {
            JSONObject data = new JSONObject();
            data.put("host", userDetail.getHost());
            data.put("token", userDetail.getToken());
            data.put("namespace", namespace);
            data.put("type", "SERVICE");
            data.put("command", "CREATE");
            data.put("body", body);
            kafkaTemplate.send(new ProducerRecord<String, JSONObject>(orderTopic, userDetail.getHost(), data));
        }
    }

    public void updateService(UserDetail userDetail, String namespace, String name, String yamlString){

        Yaml yaml = new Yaml();
        Map<String,Object> body = yaml.load(yamlString);
        userDetail = userDetailService.getUserDetail(userDetail.getUsername());
        if( userDetail.getHost() != null && userDetail.getToken() != null ) {
            JSONObject data = new JSONObject();
            data.put("host", userDetail.getHost());
            data.put("token", userDetail.getToken());
            data.put("namespace", namespace);
            data.put("name", name);
            data.put("type", "SERVICE");
            data.put("command", "UPDATE");
            data.put("body", body);
            kafkaTemplate.send(new ProducerRecord<String, JSONObject>(orderTopic, userDetail.getHost(), data));
        }
    }

    public void deleteService(UserDetail userDetail, String namespace, String name){
        userDetail = userDetailService.getUserDetail(userDetail.getUsername());
        if( userDetail.getHost() != null && userDetail.getToken() != null ) {
            JSONObject data = new JSONObject();
            data.put("host", userDetail.getHost());
            data.put("token", userDetail.getToken());
            data.put("namespace", namespace);
            data.put("name", name);
            data.put("type", "POD");
            data.put("command", "DELETE");
            kafkaTemplate.send(new ProducerRecord<String, JSONObject>(orderTopic, userDetail.getHost(), data));
        }
    }

}
