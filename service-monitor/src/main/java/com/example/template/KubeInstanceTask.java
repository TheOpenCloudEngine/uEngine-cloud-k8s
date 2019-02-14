package com.example.template;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.producer.ProducerRecord;
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
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.V1LoadBalancerIngress;
import io.kubernetes.client.models.V1Service;
import io.kubernetes.client.models.V1ServicePort;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.Watch;

@Service
@EnableScheduling
public class KubeInstanceTask implements InitializingBean {

    @Autowired
    private Environment environment;

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
    	
    	CoreV1Api av1api = new CoreV1Api();
    	
    	Watch<V1Service> watch = Watch.createWatch(
                client,
                av1api.listServiceForAllNamespacesCall(null, null, null, null, null, null, null, null, Boolean.TRUE, null, null),
                new TypeToken<Watch.Response<V1Service>>() {}.getType());
    	
    	try {
            for (Watch.Response<V1Service> item : watch) {
            	
//            	kafkaTemplate.send(new ProducerRecord<String, Watch.Response<V1Service>>(instanceTopic, item.object.getMetadata().getNamespace() , item));
            	
            	Services svs = new Services();
            	
            	svs.setProvider("K8S");
            	svs.setId(UUID.randomUUID().toString());
            	svs.setType(item.type);
            	svs.setApiVersion(item.object.getApiVersion());
            	svs.setKind(item.object.getKind());
            	
            	// service의 메타데이터 정보
            	{
    				if (item.object.getMetadata().getCreationTimestamp() != null && item.object.getMetadata().getCreationTimestamp().getMillis() != 0L) {
    					SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    					svs.setCreateTimeStamp(transFormat.format(item.object.getMetadata().getCreationTimestamp().getMillis()));
    				}
    				
    				svs.setName(item.object.getMetadata().getName());
    				svs.setNamespace(item.object.getMetadata().getNamespace());
    				svs.setUid(item.object.getMetadata().getUid());
            	}
            	
            	// service의 spec 정보
            	{
//            		svs.setStrategyType(item.object.getSpec().getStrategy().getType());
//            		svs.setSpecReplicas(item.object.getSpec().getReplicas());
            		
            		svs.setSpecSessionAffinity(item.object.getSpec().getSessionAffinity());
            		for(V1ServicePort vsport : item.object.getSpec().getPorts()) {
            			svs.setSpecPort(vsport.getPort());
            			svs.setSpecProtocol(vsport.getProtocol());
//            			svs.setSpecTargetPort(vsport.getTargetPort().getStrValue());
            		}
            		
            		svs.setSpecType(item.object.getSpec().getType());
            		
            	}
            	
            	// service의 status 정보
            	{
            		if(item.object.getStatus().getLoadBalancer() != null && item.object.getStatus().getLoadBalancer().getIngress() != null) {
            			for(V1LoadBalancerIngress v1Ingress : item.object.getStatus().getLoadBalancer().getIngress()) {
            				svs.setHostname(v1Ingress.getHostname());
            				svs.setIngressIp(v1Ingress.getIp());
            			}
            		}
            	}
            	
                kafkaTemplate.send(new ProducerRecord<String, Services>(instanceTopic, item.object.getMetadata().getNamespace() , svs));

                System.out.printf("%s : %s %n" , svs.getType(), svs.toString() );
            }
        } finally {
            watch.close();
        }
    	
    }

}
