package com.example.template.k8s.deployment;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.example.template.sse.SseBaseMessageHandler;
import com.google.gson.Gson;



@Service
public class DeploymentKafkaService {

    private static final Logger LOG = LoggerFactory.getLogger(DeploymentKafkaService.class);

    @Autowired
    private DeploymentService deploymentService;

    @Autowired
    private SseBaseMessageHandler messageHandler;


    @KafkaListener(topics = "${topic.delpoyMsgTopic}")
    public void listenByDeployment(@Payload String message) throws ParseException {
        Gson gson = new Gson();
        Deployment dpl = gson.fromJson(message, Deployment.class);
        if("DELETE".equals(dpl.getProvider())) {
            deploymentService.delete();
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
            Calendar cal = Calendar.getInstance();
            String today = null;
            today = formatter.format(cal.getTime());
            Timestamp ts = Timestamp.valueOf(today);
            dpl.setCreateTime(ts);

            deploymentService.update(dpl);
//    	messageHandler.publish(dpl.getName(), dpl.getProvider(), message);
            System.out.println(message);
            messageHandler.publish("deployment", message, dpl.getNamespace());

//        String bodyData = message.replaceAll("\\\\", "");
//        System.out.println(bodyData);

//        JSONParser parser = new JSONParser();
//        JSONObject json = (JSONObject) parser.parse(message);
//        String bodyData = json.get("object").toString();
//        bodyData = bodyData.replace("\\\"", "'");
//        bodyData = bodyData.replaceAll("\\\\", "");
//        Yaml yaml = new Yaml();
//        V1Deployment body = yaml.loadAs(bodyData, V1Deployment.class);
//        System.out.println(body);

//        Gson gson = new Gson();
//        V1Deployment dpl = gson.fromJson(message, V1Deployment.class);
//        System.out.println(dpl.toString());

//    	ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
//    	V1Deployment dpl1 = null;
//    	try {
//			dpl1 = yamlReader.readValue(message, V1Deployment.class);
//		} catch (JsonParseException e) {
//			e.printStackTrace();
//		} catch (JsonMappingException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//    	ObjectMapper mapper = new ObjectMapper();
//    	Watch.Response<V1Deployment> item = null;
//		try {
//			item = mapper.readValue(message, new TypeReference<Watch.Response<V1Deployment>>(){});
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//    	Deployment dpl = new Deployment();
//
//    	dpl.setProvider("K8S");
//    	dpl.setId(UUID.randomUUID().toString());
//    	dpl.setType(item.type);
//    	dpl.setApiVersion(item.object.getApiVersion());
//    	dpl.setKind(item.object.getKind());
//
//    	// deployment의 메타데이터 정보
//    	{
//			if (item.object.getMetadata().getCreationTimestamp() != null && item.object.getMetadata().getCreationTimestamp().getMillis() != 0L) {
//				SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//				dpl.setCreateTimeStamp(transFormat.format(item.object.getMetadata().getCreationTimestamp().getMillis()));
//			}
//
//			dpl.setName(item.object.getMetadata().getName());
//			dpl.setNamespace(item.object.getMetadata().getNamespace());
//			dpl.setUid(item.object.getMetadata().getUid());
//    	}
//
//    	// deployment의 spec 정보
//    	{
//    		dpl.setStrategyType(item.object.getSpec().getStrategy().getType());
//    		dpl.setSpecReplicas(item.object.getSpec().getReplicas());
//    	}
//
//    	// deployment의 status 정보
//    	{
//    		dpl.setStatusReplicas(item.object.getStatus().getReplicas());
//    		dpl.setStatusAvailableReplicas(item.object.getStatus().getAvailableReplicas());
//    		dpl.setStatusReadyReplicas(item.object.getStatus().getReadyReplicas());
//    		dpl.setStatusUpdateReplicas(item.object.getStatus().getUpdatedReplicas());
//    	}
//
//
//    	deploymentService.update(dpl);

        }
    }

}
