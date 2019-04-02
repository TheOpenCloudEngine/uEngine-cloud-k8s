package com.example.template.k8s.pod;

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
public class PodService {

    private static final Logger LOG = LoggerFactory.getLogger(PodService.class);

    @Value("${topic.orderTopic}")
    private String orderTopic;

    @Autowired
    KafkaTemplate kafkaTemplate;


    @Autowired
    PodRepository podRepository;

    @Autowired
    UserDetailService userDetailService;

    public Pod checkInstance(Pod pod){
        return podRepository.findById(pod.getId()).orElse(new Pod());
    }


    public Iterable<Pod> getAllPods(UserDetail userDetail){
        userDetail = userDetailService.getUserDetail(userDetail.getUsername());
        return podRepository.findByHost(userDetail.getHost());
    }

    public Iterable<Pod> getPodsByNamespace(UserDetail userDetail, String namespace){
        userDetail = userDetailService.getUserDetail(userDetail.getUsername());
        return podRepository.findByHostAndNamespace(userDetail.getHost(), namespace);
    }

    public Iterable<Pod> getPodsByNamespaceAndName(UserDetail userDetail, String namespace, String name){
        userDetail = userDetailService.getUserDetail(userDetail.getUsername());
        return podRepository.findByHostAndNamespaceAndName(userDetail.getHost(), namespace, name);
    }

    public String delete() {
        podRepository.deleteAllInQuery();
        return "";
    }

    public String deleteByHost(String host) {
        podRepository.deleteByHost(host);
        return "";
    }

    public void delete(String host, String namespace, String name) {
        podRepository.deletePod(host, namespace, name);
    }

    public void update(Pod pod) {
    	podRepository.save(pod);
    }


    public void createPod(UserDetail userDetail, String namespace, String yamlString){

        Yaml yaml = new Yaml();
        Map<String,Object> body = yaml.load(yamlString);
        userDetail = userDetailService.getUserDetail(userDetail.getUsername());
        if( userDetail.getHost() != null && userDetail.getToken() != null ){
            JSONObject data = new JSONObject();
            data.put("host", userDetail.getHost());
            data.put("token", userDetail.getToken());
            data.put("namespace", namespace);
            data.put("type", "POD");
            data.put("command", "CREATE");
            data.put("body", body);
            kafkaTemplate.send(new ProducerRecord<String, JSONObject>(orderTopic, namespace , data));
        }
    }
    public void updatePod(UserDetail userDetail, String namespace, String name, String yamlString){

        Yaml yaml = new Yaml();
        Map<String,Object> body = yaml.load(yamlString);
        userDetail = userDetailService.getUserDetail(userDetail.getUsername());

        if( userDetail.getHost() != null && userDetail.getToken() != null ) {
            JSONObject data = new JSONObject();
            data.put("host", userDetail.getHost());
            data.put("token", userDetail.getToken());
            data.put("namespace", namespace);
            data.put("name", name);
            data.put("type", "POD");
            data.put("command", "UPDATE");
            data.put("body", body);
            kafkaTemplate.send(new ProducerRecord<String, JSONObject>(orderTopic, namespace, data));
        }
    }

    public void deletePod(UserDetail userDetail, String namespace, String name){
        userDetail = userDetailService.getUserDetail(userDetail.getUsername());

        if( userDetail.getHost() != null && userDetail.getToken() != null ) {
            JSONObject data = new JSONObject();
            data.put("host", userDetail.getHost());
            data.put("token", userDetail.getToken());
            data.put("namespace", namespace);
            data.put("name", name);
            data.put("type", "POD");
            data.put("command", "DELETE");
            kafkaTemplate.send(new ProducerRecord<String, JSONObject>(orderTopic, namespace, data));
        }
    }

}
