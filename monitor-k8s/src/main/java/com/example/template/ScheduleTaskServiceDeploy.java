package com.example.template;

import com.google.gson.Gson;
import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.apis.AppsV1Api;
import io.kubernetes.client.models.V1Deployment;
import io.kubernetes.client.models.V1DeploymentList;
import io.kubernetes.client.models.V1DeploymentStatus;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ScheduleTaskServiceDeploy {

    @Autowired
    KafkaTemplate kafkaTemplate;

    @Value("${topic.delpoyMsgTopic}")
    private String instanceTopic;

    public Map<String, Object> run(ApiClient client, String host, Map<String, Object> prevData, boolean sendTopic) {
        if( prevData == null ){
            prevData = new HashMap<>();
        }
        AppsV1Api api = new AppsV1Api(client);
        V1DeploymentList list = null;
        try {
            list = api.listDeploymentForAllNamespaces(null, null, null, null, null, null, null, null, null);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        boolean init = false;
        if( prevData.size() == 0 ){
            init = true;
        }
        List<V1Deployment> sendData = new ArrayList<>();
        List<V1Deployment> deletedData = new ArrayList<>();
        Map<String, Object> currentData = new HashMap<>();

        for (V1Deployment item : list.getItems()) {
            String apiVersion = "apps/v1";
            String kind = "Deployment";
            if( item.getApiVersion() == null ){
                item.setApiVersion(apiVersion);
            }
            if( item.getKind() == null ){
                item.setKind(kind);
            }
            currentData.put(item.getMetadata().getName(), item);
            if(init){
                prevData.put(item.getMetadata().getName(), item);
                // 최초 task 기동 상태
                sendData.add(item);
            }else{
                if(!prevData.containsKey(item.getMetadata().getName())){
                    // 새로 추가됨
                    sendData.add(item);
                }else{
                    V1Deployment prevItem = (V1Deployment)prevData.get(item.getMetadata().getName());
                    if( !this.compareStatus(prevItem.getStatus(), item.getStatus())){
                        // 상태값이 변경됨 -> 수정됨
                        sendData.add(item);
                    }
                }
            }
        }

        // 이전 리스트와 현재 리스트의 데이터가 다르다면 추가 또는 삭제가 이루어 진 것이라고 판단
        // 여기서 삭제된 항목을 찾음
        // 삭제된 항목은 이전에는 있는데 현재에는 없는 항목을 찾으면 됨
        // -> 이전 키 값이 현재 키 값에 없으면
        if( prevData.size() != currentData.size()){
            Set set = prevData.keySet();
            Iterator iterator = set.iterator();
            while(iterator.hasNext()){
                String key = (String)iterator.next();
                if( !currentData.containsKey(key)){
//                        System.out.println(key +  " : 삭제됨 " );
                    V1Deployment pod = (V1Deployment)prevData.get(key);
                    deletedData.add(pod);
                }
            }
        }

        for (V1Deployment item : deletedData) {
            // CreationTimestamp, Conditions 을 제거해줘야 문제가 발생하지 않는다.
            item.getStatus().setConditions(null);

            ProducerRecord record = new ProducerRecord<String, String>(instanceTopic , null);
            Headers headers = record.headers();

            if (item.getMetadata().getCreationTimestamp() != null && item.getMetadata().getCreationTimestamp().getMillis() != 0L) {
                SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                headers.add(new RecordHeader("CreateTimeStamp", transFormat.format(item.getMetadata().getCreationTimestamp().getMillis()).getBytes(StandardCharsets.UTF_8)));
            }
            headers.add(new RecordHeader("status", "DELETED".getBytes(StandardCharsets.UTF_8)));
            System.out.printf("%s -> %s : %s %n", item.getKind(), item.getMetadata().getName(), "DELETED");
            item.getMetadata().setCreationTimestamp(null);
            item.getMetadata().deletionTimestamp(null);

            Gson gson = new Gson();
            String json = gson.toJson(item);
            if(sendTopic) {
                kafkaTemplate.send(new ProducerRecord<String, String>(instanceTopic, null, host, json, headers));
            }
        }
        for (V1Deployment item : sendData) {
            // CreationTimestamp, Conditions 을 제거해줘야 문제가 발생하지 않는다.
            item.getStatus().setConditions(null);

            ProducerRecord record = new ProducerRecord<String, String>(instanceTopic , null);
            Headers headers = record.headers();

            if (item.getMetadata().getCreationTimestamp() != null && item.getMetadata().getCreationTimestamp().getMillis() != 0L) {
                SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                headers.add(new RecordHeader("CreateTimeStamp", transFormat.format(item.getMetadata().getCreationTimestamp().getMillis()).getBytes(StandardCharsets.UTF_8)));
            }
            headers.add(new RecordHeader("status", "UPDATED".getBytes(StandardCharsets.UTF_8)));
            System.out.printf("%s -> %s : %s %n", item.getKind(), item.getMetadata().getName(), "UPDATED");
            item.getMetadata().setCreationTimestamp(null);
            item.getMetadata().deletionTimestamp(null);

            Gson gson = new Gson();
            String json = gson.toJson(item);
            if(sendTopic) {
                kafkaTemplate.send(new ProducerRecord<String, String>(instanceTopic, null, host, json, headers));
            }
        }
        // 이전 상태값에 현재 상태를 저장
        prevData = currentData;

        return prevData;
    }

    private boolean compareStatus(V1DeploymentStatus prevItem, V1DeploymentStatus nextItem){
        if (nextItem != null && prevItem != null) {
            return Objects.equals(prevItem.getAvailableReplicas(), nextItem.getAvailableReplicas()) && Objects.equals(prevItem.getCollisionCount(),  nextItem.getCollisionCount()) && Objects.equals(prevItem.getObservedGeneration(),  nextItem.getObservedGeneration()) && Objects.equals(prevItem.getReadyReplicas(),  nextItem.getReadyReplicas()) && Objects.equals(prevItem.getReplicas(),  nextItem.getReplicas()) && Objects.equals(prevItem.getUnavailableReplicas(),  nextItem.getUnavailableReplicas()) && Objects.equals(prevItem.getUpdatedReplicas(),  nextItem.getUpdatedReplicas());
        } else {
            return false;
        }

    }

}