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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sun.reflect.annotation.ExceptionProxy;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
public class ScheduleTaskService {

    @Autowired
    KafkaTemplate kafkaTemplate;

    @Value("${topic.podMsgTopic}")
    private String instanceTopic;

    @Value("${apiUrl}")
    private String apiUrl;

    @Autowired
    ScheduleTaskServicePod scheduleTaskServicePod;
    @Autowired
    ScheduleTaskServiceDeploy scheduleTaskServiceDeploy;
    @Autowired
    ScheduleTaskServiceSvc scheduleTaskServiceSvc;

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

        try {
            RestTemplate rt = new RestTemplate();
            ResponseEntity<ArrayList> response = rt.exchange(apiUrl + "/kube/user/getUserDetailList", HttpMethod.GET, new HttpEntity(null), ArrayList.class);
            ArrayList<LinkedHashMap> list = response.getBody();
            for (LinkedHashMap data : list) {
                String username = (String) data.get("username");
                String host = (String) data.get("host");
                String token = (String) data.get("token");

                System.out.println("host = " + host);
                System.out.println("token = " + token);

                if( !this.jobsMap.containsKey(host)){
                    MessageToKafkaTask messageToKafkaTask = new MessageToKafkaTask(host, token);
                    messageToKafkaTask.scheduleTaskServicePod = scheduleTaskServicePod;
                    messageToKafkaTask.scheduleTaskServiceDeploy = scheduleTaskServiceDeploy;
                    messageToKafkaTask.scheduleTaskServiceSvc = scheduleTaskServiceSvc;

                    try{
                        // pre test
                        messageToKafkaTask.run();
                        this.addTaskToScheduler( host ,  messageToKafkaTask );
                    }catch(Exception ex){

                    }

                }

            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "${topic.stateMsgTopic}")
    public void listen(@Payload String message) throws ParseException {
        System.out.println("topic message = " + message);
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(message);
        Object state = json.get("state");
        String host = (String)json.get("host");
        String token = (String)json.get("token");

        boolean importScheduler = false;
        MessageToKafkaTask messageToKafkaTask = new MessageToKafkaTask(host, token);
        messageToKafkaTask.scheduleTaskServicePod = scheduleTaskServicePod;
        messageToKafkaTask.scheduleTaskServiceDeploy = scheduleTaskServiceDeploy;
        messageToKafkaTask.scheduleTaskServiceSvc = scheduleTaskServiceSvc;
        try{
            messageToKafkaTask.run();
            if (!(state != null && "ONCE".equals(state) )) {
                importScheduler = true;
            }

        }catch (Exception e){
            System.out.println("error");
        }
//        System.out.println("host = " + host + " , importScheduler = " + importScheduler);
        if( importScheduler ) {
            this.addTaskToScheduler(host, messageToKafkaTask);
            System.out.println("jobsMap = " + jobsMap.size());
            System.out.println("jobsMap = " + jobsMap.toString());
        }
    }

    private class MessageToKafkaTask implements Runnable {

        private String host;
        private String token;
        private ApiClient client;

        ScheduleTaskServicePod scheduleTaskServicePod;
        ScheduleTaskServiceDeploy scheduleTaskServiceDeploy;
        ScheduleTaskServiceSvc scheduleTaskServiceSvc;

        // 이전 상태값을 저장하는 객체. 해당 객체에 변경값이 있을때만 메세지를 날린다.
        private Map<String, Object> podPrevData;
        private Map<String, Object> deployPrevData;
        private Map<String, Object> svcPrevData;

        public MessageToKafkaTask(String host, String token) {
            this.host = host;
            this.token = token;

            ApiClient client = Config.fromToken(this.host, this.token, false);
            this.client = client;
            client.getHttpClient().setReadTimeout(10, TimeUnit.MINUTES);

            this.podPrevData = new HashMap<>();
            this.deployPrevData = new HashMap<>();
            this.svcPrevData = new HashMap<>();
        }

        public void run() {
            this.podPrevData = this.scheduleTaskServicePod.run(this.client, this.host, this.podPrevData);
            this.deployPrevData = this.scheduleTaskServiceDeploy.run(this.client, this.host, this.deployPrevData);
            this.svcPrevData = this.scheduleTaskServiceSvc.run(this.client, this.host, this.svcPrevData);
        }
    }
}