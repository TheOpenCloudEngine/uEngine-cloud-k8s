package com.example.template.k8s.kafka;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.example.template.k8s.deployment.Deployment;
import com.example.template.k8s.deployment.DeploymentService;
import io.kubernetes.client.models.V1Deployment;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
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


//    @KafkaListener(topics = "${topic.delpoyMsgTopic}")
    public void listenByDeploymentOld(@Payload String message) throws ParseException {
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
            messageHandler.publish("deployment", message, dpl.getNamespace(), dpl.getHost());

        }
    }

    @KafkaListener(topics = "${topic.delpoyMsgTopic}")
    public void listenByDeployment(@Payload String message, ConsumerRecord<?, ?> consumerRecord) throws ParseException {
        String host = (String)consumerRecord.key();
        System.out.println("===================");
        System.out.println("host = " + host);
        System.out.println("===================");
        Header[] h = consumerRecord.headers().toArray();
        // 객체의 DataTime 이 정상적으로 변환이 안되서 header 에 담아서 처리함
        String createTimeStamp = null;
        String status = null;
        for (Header header : h) {
            if (header.key().equals("CreateTimeStamp")) {
                createTimeStamp = new String(header.value(), StandardCharsets.UTF_8);
            }
            if (header.key().equals("status")) {
                status = new String(header.value(), StandardCharsets.UTF_8);
            }
        }

        Gson gson = new Gson();
        V1Deployment item = gson.fromJson(message, V1Deployment.class);
        String namespace = item.getMetadata().getNamespace();
        String name = item.getMetadata().getName();

        Deployment dpl = new Deployment();

        dpl.setProvider("K8S");
        dpl.setName(item.getMetadata().getName());
        dpl.setNamespace(item.getMetadata().getNamespace());

        if("DELETED".equals(status)) {
            deploymentService.delete(host, namespace, name);
            dpl.setApiVersion(status);

        }else {

            dpl.setId(item.getMetadata().getUid());
            dpl.setHost(host);
            dpl.setApiVersion(item.getApiVersion());
            dpl.setKind(item.getKind());

            // deployment의 메타데이터 정보
            {
                dpl.setCreateTimeStamp(createTimeStamp);
            }

            // deployment의 spec 정보
            {
                dpl.setStrategyType(item.getSpec().getStrategy().getType());
                dpl.setSpecReplicas(item.getSpec().getReplicas());
            }

            // deployment의 status 정보
            {
                dpl.setStatusReplicas(item.getStatus().getReplicas());
                dpl.setStatusAvailableReplicas(item.getStatus().getAvailableReplicas());
                dpl.setStatusReadyReplicas(item.getStatus().getReadyReplicas());
                dpl.setStatusUpdateReplicas(item.getStatus().getUpdatedReplicas());
            }

            {
                dpl.setSourceData(new Gson().toJson(item));
            }
            {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
                Calendar cal = Calendar.getInstance();
                String today = null;
                today = formatter.format(cal.getTime());
                Timestamp ts = Timestamp.valueOf(today);
                dpl.setCreateTime(ts);
            }

            deploymentService.update(dpl);
        }
        String json = gson.toJson(dpl);
        messageHandler.publish("deployment", json, dpl.getNamespace(), host);
    }

}
