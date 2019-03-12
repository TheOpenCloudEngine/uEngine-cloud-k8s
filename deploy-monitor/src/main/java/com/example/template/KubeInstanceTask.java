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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

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
        this.host = environment.getProperty("kubehost");
        this.token = environment.getProperty("kubetoken");

        // kubenate Configuration setting
        ApiClient client = Config.fromToken(this.getHost(), this.getToken(), false);
        this.client = client;
        client.getHttpClient().setReadTimeout(60, TimeUnit.MINUTES);
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
    	ObjectMapper objectMapper = new ObjectMapper();
    	
    	AppsV1Api av1api = new AppsV1Api();
    	
    	Watch<V1Deployment> watch = Watch.createWatch(
                client,
                av1api.listDeploymentForAllNamespacesCall(null, null, null, null, null, null, null, null, Boolean.TRUE, null, null),
                new TypeToken<Watch.Response<V1Deployment>>() {}.getType());
    	
    	try {
            for (Watch.Response<V1Deployment> item : watch) {

//                Yaml yaml = new Yaml();
//                String data = yaml.dump(item.object);
//                Map<String,Object> map = (Map<String, Object>) yaml.load(data);

//                V1Deployment deploy = item.object;
//                deploy.getMetadata().setCreationTimestamp(null);
//                deploy.getStatus().setConditions(null);
//
//                Gson gson = new Gson();
//                String data = gson.toJson(item.object);
//                JsonObject request = new JsonParser().parse(data).getAsJsonObject();
//                System.out.printf("%s %n" , data );
//                JSONObject data = new JSONObject();

//                Yaml yaml = new Yaml();
//                V1Deployment body = yaml.loadAs(data, V1Deployment.class);
//                System.out.println(body);
//            	kafkaTemplate.send(new ProducerRecord<String, String>(instanceTopic, item.object.getMetadata().getUid() , data));

                
            	Deployment dpl = new Deployment();
            	
            	dpl.setProvider("K8S");
            	dpl.setId(UUID.randomUUID().toString());
            	dpl.setType(item.type);
            	dpl.setApiVersion(item.object.getApiVersion());
            	dpl.setKind(item.object.getKind());
            	
            	// deployment의 메타데이터 정보
            	{
    				if (item.object.getMetadata().getCreationTimestamp() != null && item.object.getMetadata().getCreationTimestamp().getMillis() != 0L) {
    					SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    					dpl.setCreateTimeStamp(transFormat.format(item.object.getMetadata().getCreationTimestamp().getMillis()));
    				}
    				
    				dpl.setName(item.object.getMetadata().getName());
    				dpl.setNamespace(item.object.getMetadata().getNamespace());
    				dpl.setUid(item.object.getMetadata().getUid());
            	}
            	
            	// deployment의 spec 정보
            	{
            		dpl.setStrategyType(item.object.getSpec().getStrategy().getType());
            		dpl.setSpecReplicas(item.object.getSpec().getReplicas());
            	}
            	
            	// deployment의 status 정보
            	{
            		dpl.setStatusReplicas(item.object.getStatus().getReplicas());
            		dpl.setStatusAvailableReplicas(item.object.getStatus().getAvailableReplicas());
            		dpl.setStatusReadyReplicas(item.object.getStatus().getReadyReplicas());
            		dpl.setStatusUpdateReplicas(item.object.getStatus().getUpdatedReplicas());
            	}
            	
            	{
            		// CreationTimestamp, Conditions 을 제거해줘야 문제가 발생하지 않는다.
					item.object.getMetadata().setCreationTimestamp(null);
                    item.object.getMetadata().getAnnotations().put("kubectl.kubernetes.io/last-applied-configuration", null);

//					item.object.getStatus().setConditions(null);
					item.object.setStatus(null);
					dpl.setSourceData(new Gson().toJson(item.object));
            	}

                kafkaTemplate.send(new ProducerRecord<String, Deployment>(instanceTopic, item.object.getMetadata().getNamespace() , dpl));
                
                System.out.printf("%s : %s %n" , dpl.getType(), dpl.toString() );
                
            }
        } finally {
            watch.close();
        }
    	
    }

}
