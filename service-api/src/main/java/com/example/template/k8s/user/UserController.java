package com.example.template.k8s.user;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/kube/user")
public class UserController {

    @Autowired
    KafkaTemplate kafkaTemplate;
    @Autowired
    UserDetailService userDetailService;


    @Value("${topic.stateMsgTopic}")
    private String stateMsgTopic;

    @RequestMapping(value = "/getUserDetail", method = RequestMethod.GET)
    public UserDetail getUserDetail(HttpServletRequest request, @RequestParam String username) {
        //로그인한 사용자 ID 가져오기
        if( username != null) {
//            System.out.println(username);
            UserDetail userDetail = userDetailService.getUserDetail(username);

            if( userDetail.getHost() != null && userDetail.getToken() != null ) {
                JSONObject data = new JSONObject();
                data.put("host", userDetail.getHost());
                data.put("token", userDetail.getToken());
                // 모니터링 서비스에게, 해당 host 의 데이터를 수집하라고 명령함.
                kafkaTemplate.send(new ProducerRecord<String, JSONObject>(stateMsgTopic, userDetail.getHost(), data));
            }


            return userDetail;
        }

        return null;
    }

    @RequestMapping(value = "/saveUserDetail", method = RequestMethod.PUT)
    public void saveUserDetails(HttpServletRequest request,
                                @RequestBody Map<String, Object> param ) {
        if (param != null) {
            String username = (String) param.get("username");
            String host = (String) param.get("host");
            String token = (String) param.get("token");

            if( host == null || "".equals(host)){
                throw new RuntimeException("host is null");
            }
            if( token == null || "".equals(token)){
                throw new RuntimeException("token is null");
            }

            // user 가 있으면 큐에 등록하여 정기적으로 메세지를 받는다.
            if (username != null) {
                UserDetail userDetail = new UserDetail();
                userDetail.setUsername(username);
                userDetail.setHost(host);
                userDetail.setToken(token);

                userDetailService.save(userDetail);

                JSONObject data = new JSONObject();
                data.put("host", userDetail.getHost());
                data.put("token", userDetail.getToken());

                // 모니터링 서비스에게, 해당 host 의 데이터를 수집하라고 명령함.
                kafkaTemplate.send(new ProducerRecord<String, JSONObject>(stateMsgTopic, userDetail.getHost(), data));

            } else {
                // 회원이 아니면, 한번 호출만 하고 끝낸다.
                this.getOnceClusterCall(request, param);
            }
        }
    }

    @RequestMapping(value = "/getOnceClusterCall", method = RequestMethod.PUT)
    public void getOnceClusterCall(HttpServletRequest request,
            @RequestBody Map<String, Object> param ){

        String host = (String) param.get("host");
        String token = (String) param.get("token");

        JSONObject data = new JSONObject();
        data.put("state", "ONCE");
        data.put("host", host);
        data.put("token", token);

        kafkaTemplate.send(new ProducerRecord<String, JSONObject>(stateMsgTopic, host, data));

    }
}
