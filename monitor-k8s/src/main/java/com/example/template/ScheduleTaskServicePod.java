package com.example.template;

import com.google.gson.Gson;
import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.V1Pod;
import io.kubernetes.client.models.V1PodList;
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
public class ScheduleTaskServicePod {

    @Autowired
    KafkaTemplate kafkaTemplate;

    @Value("${topic.podMsgTopic}")
    private String instanceTopic;

    public Map<String, Object> run(ApiClient client, String host, Map<String, Object> prevData) {
        if( prevData == null ){
            prevData = new HashMap<>();
        }
        
        CoreV1Api api = new CoreV1Api(client);
        V1PodList list = null;
        try {
    //                list = api.listNamespacedPod("default", null, null, null, null, null, null, null, null, null);
            list = api.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        boolean init = false;
        if( prevData.size() == 0 ){
            init = true;
        }
        List<V1Pod> sendData = new ArrayList<>();
        List<V1Pod> deletedData = new ArrayList<>();
        Map<String, Object> currentData = new HashMap<>();

        for (V1Pod item : list.getItems()) {
            String apiVersion = "v1";
            String kind = "Pod";
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
                    V1Pod prevItem = (V1Pod)prevData.get(item.getMetadata().getName());
                    if( !(prevItem.getStatus().getPhase().equals(item.getStatus().getPhase()))){
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
                    V1Pod pod = (V1Pod)prevData.get(key);
                    deletedData.add(pod);
                }
            }
        }

        // 헤더를 맞추기 위하여 삭제된 데이터를 다로 보냄
        for (V1Pod item : deletedData) {
            ProducerRecord record = new ProducerRecord<String, String>(instanceTopic , null);
            Headers headers = record.headers();

            if (item.getMetadata().getCreationTimestamp() != null && item.getMetadata().getCreationTimestamp().getMillis() != 0L) {
                SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                headers.add(new RecordHeader("CreateTimeStamp", transFormat.format(item.getMetadata().getCreationTimestamp().getMillis()).getBytes(StandardCharsets.UTF_8)));
            }
            headers.add(new RecordHeader("status", "DELETED".getBytes(StandardCharsets.UTF_8)));
            System.out.printf("%s -> %s : %s %n", item.getKind(), item.getMetadata().getName(), "DELETED");
            // CreationTimestamp, Conditions 을 제거해줘야 문제가 발생하지 않는다.
            item.getMetadata().setCreationTimestamp(null);
            item.getMetadata().deletionTimestamp(null);
            item.getStatus().setConditions(null);
            item.getStatus().setInitContainerStatuses(null);
            item.getStatus().setContainerStatuses(null);
            item.getStatus().setStartTime(null);

            Gson gson = new Gson();
            String json = gson.toJson(item);
            kafkaTemplate.send(new ProducerRecord<String, String>(instanceTopic, null, host , json , headers));
        }

        for (V1Pod item : sendData) {
            ProducerRecord record = new ProducerRecord<String, String>(instanceTopic , null);
            Headers headers = record.headers();

            if (item.getMetadata().getCreationTimestamp() != null && item.getMetadata().getCreationTimestamp().getMillis() != 0L) {
                SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                headers.add(new RecordHeader("CreateTimeStamp", transFormat.format(item.getMetadata().getCreationTimestamp().getMillis()).getBytes(StandardCharsets.UTF_8)));
            }
            headers.add(new RecordHeader("status", item.getStatus().getPhase().getBytes(StandardCharsets.UTF_8)));

            System.out.printf("%s -> %s : %s %n", item.getKind(), item.getMetadata().getName(), "UPDATED");
            // CreationTimestamp, Conditions 을 제거해줘야 문제가 발생하지 않는다.
            item.getMetadata().setCreationTimestamp(null);
            item.getMetadata().deletionTimestamp(null);
            item.getStatus().setConditions(null);
            item.getStatus().setInitContainerStatuses(null);
            item.getStatus().setContainerStatuses(null);
            item.getStatus().setStartTime(null);

            Gson gson = new Gson();
            String json = gson.toJson(item);
            kafkaTemplate.send(new ProducerRecord<String, String>(instanceTopic, null, host , json , headers));
        }
        // 이전 상태값에 현재 상태를 저장
        prevData = currentData;
        return prevData;
    }
}