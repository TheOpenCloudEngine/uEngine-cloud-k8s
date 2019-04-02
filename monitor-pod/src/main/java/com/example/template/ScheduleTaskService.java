package com.example.template;

import com.google.gson.Gson;
import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.V1Pod;
import io.kubernetes.client.models.V1PodList;
import io.kubernetes.client.util.Config;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
public class ScheduleTaskService {

    @Autowired
    KafkaTemplate kafkaTemplate;

    @Value("${topic.instanceTopic}")
    private String instanceTopic;

    // Task Scheduler
    TaskScheduler scheduler;
    // A map for keeping scheduled tasks
    Map<String, ScheduledFuture<?>> jobsMap = new HashMap<>();

    public ScheduleTaskService(TaskScheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void addTaskToScheduler(String id, Runnable task) {
        ScheduledFuture<?> scheduledTask = scheduler.schedule(task, new PeriodicTrigger(3, TimeUnit.SECONDS));
        jobsMap.put(id, scheduledTask);
    }

    // Remove scheduled task
    public void removeTaskFromScheduler(String id) {
        ScheduledFuture<?> scheduledTask = jobsMap.get(id);
        if(scheduledTask != null) {
            scheduledTask.cancel(true);
            jobsMap.put(id, null);
        }
    }

    // A context refresh event listener
    @EventListener({ ContextRefreshedEvent.class })
    void contextRefreshedEvent() {
        // Get all tasks from DB and reschedule them in case of context restarted

        System.out.println("==========================");
        System.out.println("========  시작   ==========");
        System.out.println("==========================");

        String host = "https://api.k8s.bzdvops.com";

        kafkaTemplate.send(new ProducerRecord<String, String>(instanceTopic, host , "DELETE_ALL"));

        // TODO 삭제
        String token = "eyJhbGciOiJSUzI1NiIsImtpZCI6IiJ9.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJkZWZhdWx0Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZWNyZXQubmFtZSI6ImNsb3VkdXNlci10b2tlbi16cmtqaiIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50Lm5hbWUiOiJjbG91ZHVzZXIiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC51aWQiOiI0ZmJmNzk0YS0zNTgwLTExZTktYTU2OC0wMjkxMGMyMWIzOTgiLCJzdWIiOiJzeXN0ZW06c2VydmljZWFjY291bnQ6ZGVmYXVsdDpjbG91ZHVzZXIifQ.APncfC7biCYEre4LZ3S-TVcf641qpQlo7r_BN0khN8ovnT7rR3DWGTaUDLP2eFQBLUvEVSAgTT1g0wF1OFsqEx-Sn3fHByyf1r8t15wvN_XJFM2_V_ZZBosUeZCciklcky0jwF6AWcSpUo9nKa23yBtylJ9d6EPjAq8KtURdX7IVb5i8j0InSExyOQZv5xJ-yv55GB_yRrI9rQ6cwxt_PdFaQiFLjSjnp6SvZj3nPACw_qb98w2I4p_O8DZ5SE-b4OetZj0xmZM7ELXBbceMDepT0UHrU1ZcIc54aWNnhyGFdxspxwrGWSDtNL4T6KKbTqU6HVXkiJTKCw1w9E9hHg";
        this.addTaskToScheduler( host ,  new MessageToKafkaTask(host, token) );

    }

    static int index = 0;

    @KafkaListener(topics = "${topic.stateMsgTopic}")
    public void listen(@Payload String message) throws ParseException {
        System.out.println("topic message = " + message);
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(message);
        Object state = json.get("state");
        String host = (String)json.get("host");
        String token = (String)json.get("token");
        if( state != null && "ONCE".equals(state)){
            // TODO 한번만 호출하는 로직
            System.out.println("ONCE = " + "ONCE");
        }else{
            this.addTaskToScheduler( host ,  new MessageToKafkaTask(host, token) );
            System.out.println("jobsMap = " + jobsMap.size());
            System.out.println("jobsMap = " + jobsMap.toString());
        }
    }

    private class MessageToKafkaTask implements Runnable {

        private String host;
        private String token;
        private ApiClient client;
        // 이전 상태값을 저장하는 객체. 해당 객체에 변경값이 있을때만 메세지를 날린다.
        private Map<String, Object> prevData;

        public MessageToKafkaTask(String host, String token) {
            this.host = host;
            this.token = token;

            ApiClient client = Config.fromToken(this.host, this.token, false);
            this.client = client;
            client.getHttpClient().setReadTimeout(10, TimeUnit.MINUTES);
            this.prevData = new HashMap<>();
        }

        public void run() {

            CoreV1Api api = new CoreV1Api(this.client);
            V1PodList list =
                    null;
            try {
//                list = api.listNamespacedPod("default", null, null, null, null, null, null, null, null, null);
                list = api.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null);
            } catch (ApiException e) {
                e.printStackTrace();
            }
            boolean init = false;
            if( this.prevData.size() == 0 ){
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
                    this.prevData.put(item.getMetadata().getName(), item);
                    // 최초 task 기동 상태
                    sendData.add(item);
                }else{
                    if(!this.prevData.containsKey(item.getMetadata().getName())){
                        // 새로 추가됨
                        sendData.add(item);
                    }else{
                        V1Pod prevItem = (V1Pod)this.prevData.get(item.getMetadata().getName());
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
            if( this.prevData.size() != currentData.size()){
                Set set = this.prevData.keySet();
                Iterator iterator = set.iterator();
                while(iterator.hasNext()){
                    String key = (String)iterator.next();
                    if( !currentData.containsKey(key)){
//                        System.out.println(key +  " : 삭제됨 " );
                        V1Pod pod = (V1Pod)this.prevData.get(key);
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
                System.out.printf("%s : %s %n", item.getMetadata().getName(), "DELETED");
                // CreationTimestamp, Conditions 을 제거해줘야 문제가 발생하지 않는다.
                item.getMetadata().setCreationTimestamp(null);
                item.getMetadata().deletionTimestamp(null);
                item.getStatus().setConditions(null);
                item.getStatus().setInitContainerStatuses(null);
                item.getStatus().setContainerStatuses(null);
                item.getStatus().setStartTime(null);

                Gson gson = new Gson();
                String json = gson.toJson(item);
                kafkaTemplate.send(new ProducerRecord<String, String>(instanceTopic, null, this.host , json , headers));
            }

            for (V1Pod item : sendData) {
                ProducerRecord record = new ProducerRecord<String, String>(instanceTopic , null);
                Headers headers = record.headers();

                if (item.getMetadata().getCreationTimestamp() != null && item.getMetadata().getCreationTimestamp().getMillis() != 0L) {
                    SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    headers.add(new RecordHeader("CreateTimeStamp", transFormat.format(item.getMetadata().getCreationTimestamp().getMillis()).getBytes(StandardCharsets.UTF_8)));
                }
                headers.add(new RecordHeader("status", item.getStatus().getPhase().getBytes(StandardCharsets.UTF_8)));

                System.out.printf("%s : %s %n", item.getMetadata().getName(), "UPDATED");
                // CreationTimestamp, Conditions 을 제거해줘야 문제가 발생하지 않는다.
                item.getMetadata().setCreationTimestamp(null);
                item.getMetadata().deletionTimestamp(null);
                item.getStatus().setConditions(null);
                item.getStatus().setInitContainerStatuses(null);
                item.getStatus().setContainerStatuses(null);
                item.getStatus().setStartTime(null);

                Gson gson = new Gson();
                String json = gson.toJson(item);
                kafkaTemplate.send(new ProducerRecord<String, String>(instanceTopic, null, this.host , json , headers));
            }
            // 이전 상태값에 현재 상태를 저장
            this.prevData = currentData;
        }
    }
}