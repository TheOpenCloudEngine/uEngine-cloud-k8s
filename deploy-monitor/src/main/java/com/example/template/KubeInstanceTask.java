package com.example.template;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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

import com.google.common.reflect.TypeToken;

import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.AppsV1Api;
import io.kubernetes.client.models.V1Deployment;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.Watch;

@Service
@EnableScheduling
public class KubeInstanceTask implements InitializingBean {

    @Autowired
    Environment environment;

    @Value("${topic.instanceTopic}")
    private String instanceTopic;

    @Autowired
    private KafkaTemplate kafkaTemplate;

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
    	
    	AppsV1Api av1api = new AppsV1Api();
    	
    	Watch<V1Deployment> watch = Watch.createWatch(
                client,
                av1api.listDeploymentForAllNamespacesCall(null, null, null, null, null, null, null, null, Boolean.TRUE, null, null),
                new TypeToken<Watch.Response<V1Deployment>>() {}.getType());
    	
    	try {
            for (Watch.Response<V1Deployment> item : watch) {
            	
            	Deployment dpl = new Deployment();
            	
            	dpl.setProvider("K8S");
            	dpl.setId(UUID.randomUUID().toString());
            	dpl.setType(item.type);
            	dpl.setApiVersion(item.object.getApiVersion());
            	dpl.setKind(item.object.getKind());
            	
            	{
    				if (item.object.getMetadata().getCreationTimestamp() != null && item.object.getMetadata().getCreationTimestamp().getMillis() != 0L) {
    					SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    					dpl.setCreateTimeStamp(transFormat.format(item.object.getMetadata().getCreationTimestamp().getMillis()));
    				}
    				
    				dpl.setName(item.object.getMetadata().getName());
    				dpl.setNamespace(item.object.getMetadata().getNamespace());
    				dpl.setUid(item.object.getMetadata().getUid());
            	}
            	
            	{
            		dpl.setStrategyType(item.object.getSpec().getStrategy().getType());
            		dpl.setSpecReplicas(item.object.getSpec().getReplicas());
            	}
            	
            	{
            		dpl.setStatusReplicas(item.object.getStatus().getReplicas());
            		dpl.setStatusAvailableReplicas(item.object.getStatus().getAvailableReplicas());
            		dpl.setStatusReadyReplicas(item.object.getStatus().getReadyReplicas());
            		dpl.setStatusUpdateReplicas(item.object.getStatus().getUpdatedReplicas());
            	}
            	
                kafkaTemplate.send(new ProducerRecord<String, Deployment>(instanceTopic, item.object.getMetadata().getNamespace() , dpl));

                System.out.printf("%s : %s %n" , dpl.getType(), dpl.toString() );
            }
        } finally {
            watch.close();
        }
    	
    }

}
