package com.example.template.k8s.deployment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import com.example.template.k8s.pod.LogMessageFormat;
import com.example.template.k8s.pod.PodService;
import com.example.template.k8s.user.UserDetail;
import com.example.template.k8s.user.UserDetailService;
import java.util.Optional;

import com.example.template.k8s.pod.Pod;

@Service
public class DeploymentService {

    private static final Logger LOG = LoggerFactory.getLogger(DeploymentService.class);

    @Autowired
    DeploymentRepository deploymentRepository;

    @Autowired
    UserDetailService userDetailService;

    @Value("${topic.orderTopic}")
    private String orderTopic;

    @Autowired
    KafkaTemplate kafkaTemplate;

    public Deployment checkInstance(Deployment deployment){
        return deploymentRepository.findById(deployment.getId()).orElse(new Deployment());
    }

    public Iterable<Deployment> getAllDeployment(UserDetail userDetail){
        userDetail = userDetailService.getUserDetail(userDetail.getUsername());
        return deploymentRepository.findByHost(userDetail.getHost());
    }

    public Iterable<Deployment> getDeploymentByNamespace(UserDetail userDetail, String namespace){
        userDetail = userDetailService.getUserDetail(userDetail.getUsername());
        return deploymentRepository.findByHostAndNamespace(userDetail.getHost(), namespace);
    }

    public Iterable<Deployment> getDeploymentByNamespaceAndName(UserDetail userDetail, String namespace, String name){
        userDetail = userDetailService.getUserDetail(userDetail.getUsername());
        return deploymentRepository.findByHostAndNamespaceAndName(userDetail.getHost(), namespace, name);
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

    public void createDeploy(UserDetail userDetail, String namespace, String yamlString){

        Yaml yaml = new Yaml();
        Map<String,Object> body = yaml.load(yamlString);

        userDetail = userDetailService.getUserDetail(userDetail.getUsername());
        if( userDetail.getHost() != null && userDetail.getToken() != null ) {
            JSONObject data = new JSONObject();
            data.put("host", userDetail.getHost());
            data.put("token", userDetail.getToken());
            data.put("namespace", namespace);
            data.put("type", "DEPLOY");
            data.put("command", "CREATE");
            data.put("body", body);
            kafkaTemplate.send(new ProducerRecord<String, JSONObject>(orderTopic, namespace, data));
        }
    }
    public void updateDeploy(UserDetail userDetail, String namespace, String name, String yamlString){

        Yaml yaml = new Yaml();

        Map<String,Object> body = yaml.load(yamlString);
        userDetail = userDetailService.getUserDetail(userDetail.getUsername());

        if( userDetail.getHost() != null && userDetail.getToken() != null ) {
            JSONObject data = new JSONObject();
            data.put("host", userDetail.getHost());
            data.put("token", userDetail.getToken());
            data.put("namespace", namespace);
            data.put("name", name);
            data.put("type", "DEPLOY");
            data.put("command", "UPDATE");
            data.put("body", body);
            kafkaTemplate.send(new ProducerRecord<String, JSONObject>(orderTopic, namespace, data));
        }
    }

    public void deleteDeploy(UserDetail userDetail, String namespace, String name){
        userDetail = userDetailService.getUserDetail(userDetail.getUsername());
        if( userDetail.getHost() != null && userDetail.getToken() != null ) {
            JSONObject data = new JSONObject();
            data.put("host", userDetail.getHost());
            data.put("token", userDetail.getToken());
            data.put("namespace", namespace);
            data.put("name", name);
            data.put("type", "DEPLOY");
            data.put("command", "DELETE");
            kafkaTemplate.send(new ProducerRecord<String, JSONObject>(orderTopic, namespace, data));
        }
    }

    @Autowired
    PodService podService;
    
    public HashMap<String, ArrayList<LogMessageFormat>> getLog(Optional<UserDetail> userDetail, String namespace, String name) {
    	
    	HashMap<String, ArrayList<LogMessageFormat>> hm = new HashMap<String, ArrayList<LogMessageFormat>>();
    	Iterable<Pod> pods = podService.getPodsByNamespaceAndNameLike(namespace, name);
    	for(Pod pod: pods) {
    		hm.put(pod.getName(), podService.getLog(userDetail, pod.getNamespace(), pod.getName()));
    	}
    	
    	return hm;
    }


}
