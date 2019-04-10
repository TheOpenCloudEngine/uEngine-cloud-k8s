package com.example.template.k8s.pod;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import io.kubernetes.client.models.V1Container;
import io.kubernetes.client.models.V1Pod;
import io.kubernetes.client.models.V1PodCondition;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.example.template.k8s.deployment.Deployment;
import com.example.template.k8s.deployment.DeploymentService;
import com.example.template.k8s.pod.PodService;
import com.example.template.sse.SseBaseMessageHandler;
import com.google.gson.Gson;



@Service
public class PodKafkaService {

    private static final Logger LOG = LoggerFactory.getLogger(PodKafkaService.class);

    @Autowired
    PodService podService;


    @Autowired
    private SseBaseMessageHandler messageHandler;



    /**
     * 특정 클래스로 받으려면 아래와 같이 객체로 변환을 하면 된다.
     * 주의점은 프로듀서쪽의 객체를 잘 마추어야 한다. - 별로 추천하지 않음
     * @param message
     * @param consumerRecord
     */

//    @KafkaListener(topics = "${topic.podeMsgTopic}")
    public void listenByObjectOld(@Payload String message, ConsumerRecord<?, ?> consumerRecord) {
        System.out.println(message);
        Gson gson = new Gson();
        Pod pod = gson.fromJson(message, Pod.class);
        if("DELETE".equals(pod.getProvider())) {
            podService.delete();
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss.SSS");
            Calendar cal = Calendar.getInstance();
            String today = null;
            today = formatter.format(cal.getTime());
            Timestamp ts = Timestamp.valueOf(today);
            pod.setCreateTime(ts);

            podService.update(pod);

            messageHandler.publish("pod", message, pod.getNamespace(), pod.getHost());

        }
    }

    @KafkaListener(topics = "${topic.podMsgTopic}")
    public void listenByObject(@Payload String message, ConsumerRecord<?, ?> consumerRecord) {

        String host = (String)consumerRecord.key();

        Header[] h = consumerRecord.headers().toArray();
        // V1Pod 객체의 DataTime 이 정상적으로 변환이 안되서 header 에 담아서 처리함
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

        if( "DELETE_ALL".equals(message)){
            podService.deleteByHost(host);
            return;
        }


        Gson gson = new Gson();
        V1Pod item = gson.fromJson(message, V1Pod.class);
        String namespace = item.getMetadata().getNamespace();
        String name = item.getMetadata().getName();


        Pod pod = new Pod();
        pod.setProvider("K8S");
        pod.setName(item.getMetadata().getName());
        pod.setNamespace(item.getMetadata().getNamespace());

        if("DELETED".equals(status)) {
            podService.delete(host, namespace, name);
            pod.setApiVersion(status);
        } else {

            pod.setId(item.getMetadata().getUid());
            pod.setHost(host);

            pod.setApiVersion(item.getApiVersion());
            pod.setKind(item.getKind());

            {
                pod.setCreateTimeStamp(createTimeStamp);
            }

            {
                pod.setNodeName(item.getSpec().getNodeName());
                pod.setRestartPolicy(item.getSpec().getRestartPolicy());
                pod.setServiceAccount(item.getSpec().getServiceAccount());
                if (item.getSpec().getContainers() != null && item.getSpec().getContainers().size() > 0) {
                    pod.setImage(item.getSpec().getContainers().get(0).getImage());
                }

            }

            {
                pod.setHostIp(item.getStatus().getHostIP());
                pod.setPodIp(item.getStatus().getPodIP());
            }

            {
                pod.setStatus(status);
                item.setStatus(null);

                pod.setSourceData(new Gson().toJson(item));
            }

            {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
                Calendar cal = Calendar.getInstance();
                String today = null;
                today = formatter.format(cal.getTime());
                Timestamp ts = Timestamp.valueOf(today);
                pod.setCreateTime(ts);
            }

            podService.update(pod);

        }

        String json = gson.toJson(pod);
        messageHandler.publish("pod", json, pod.getNamespace(), host);
    }

}
