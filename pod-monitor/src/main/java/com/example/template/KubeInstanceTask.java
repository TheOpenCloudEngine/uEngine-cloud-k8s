package com.example.template;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.*;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.Watch;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.joda.time.DateTime;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@EnableScheduling
public class KubeInstanceTask implements InitializingBean {

    @Autowired
    Environment environment;

    @Value("${topic.instanceTopic}")
    private String instanceTopic;

    @Autowired
    KafkaTemplate kafkaTemplate;

    private String host;
    private String token;
    private ApiClient client;


    @Override
    public void afterPropertiesSet() throws Exception {
        this.host = environment.getProperty("kube.host");
        this.token = environment.getProperty("kube.token");

        // kubenate Configuration setting
        ApiClient client = Config.fromToken(this.getHost(), this.getToken(), false);
        this.client = client;
        client.getHttpClient().setReadTimeout(10000, TimeUnit.MINUTES);
        Configuration.setDefaultApiClient(client);
    }

    public String getHost(){
        return this.host;
    }

    public String getToken(){
        return this.token;
    }



    @Scheduled(fixedRate = 10000)
    public void watchPod() throws IOException, ApiException{
        CoreV1Api api = new CoreV1Api();
        Watch<V1Pod> watch =
                Watch.createWatch(
                        client,
                        api.listPodForAllNamespacesCall(
                                null, null, null, null, null, null, null, null, Boolean.TRUE, null, null),
                        new TypeToken<Watch.Response<V1Pod>>() {}.getType());

        try {
            for (Watch.Response<V1Pod> item : watch) {
                Pod pod = new Pod();
                
                pod.setProvider("K8S");
                pod.setId(UUID.randomUUID().toString());
                pod.setType(item.type);
                
                {
    				if (item.object.getMetadata().getCreationTimestamp() != null && item.object.getMetadata().getCreationTimestamp().getMillis() != 0L) {
    					SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    					pod.setCreateTimeStamp(transFormat.format(item.object.getMetadata().getCreationTimestamp().getMillis()));
    				}
    				
    				pod.setName(item.object.getMetadata().getName());
    				pod.setNamespace(item.object.getMetadata().getNamespace());
    				pod.setUid(item.object.getMetadata().getUid());
            	}
                
                {
                	pod.setNodeName(item.object.getSpec().getNodeName());
                	pod.setRestartPolicy(item.object.getSpec().getRestartPolicy());
                	pod.setServiceAccount(item.object.getSpec().getServiceAccount());
                	if(item.object.getSpec().getContainers() != null) {
                		for(V1Container coctainer : item.object.getSpec().getContainers()) {
                			pod.setImage(coctainer.getImage());
                		}
                	}
                		
                }
                
                {
                	pod.setHostIp(item.object.getStatus().getHostIP());
                	pod.setPodIp(item.object.getStatus().getPodIP());
                	if(item.object.getStatus().getConditions() != null) {
                		for(V1PodCondition podcondition : item.object.getStatus().getConditions())
                		{
                			
                		}
                	}
                }
                
                {
            		// CreationTimestamp, Conditions 을 제거해줘야 문제가 발생하지 않는다.
					item.object.getMetadata().setCreationTimestamp(null);
//					item.object.getStatus().setConditions(null);
					
					pod.setStatus(item.object.getStatus().getPhase());
					item.object.setStatus(null);
					
					pod.setSourceData(new Gson().toJson(item.object));
            	}
                
                

//                JSONObject properties = new JSONObject();
//                properties.put("apiVersion", item.object.getApiVersion());
//                properties.put("image", item.object.getSpec().getContainers() != null ? item.object.getSpec().getContainers().get(0).getImage() : "");
//                // TODO set all info
//
//                pod.setProperties(properties.toJSONString());
//
//                if(item.type.equals("DELETED")){
//                    pod.setInstanceState(InstanceState.DELETE);
//                }else if(item.type.equals("MODIFIED")) {
//                    pod.setInstanceState(InstanceState.MODIFY);
//                }
//                System.out.println("Message: " + item.type + " sent to topic: " + item.object.getMetadata().getName());
                
                kafkaTemplate.send(new ProducerRecord<String, Pod>(instanceTopic, item.object.getMetadata().getNamespace() , pod));

                System.out.printf("%s : %s %n" , item.type, pod.toString() );
            }
        } finally {
            watch.close();
        }
    }

}
