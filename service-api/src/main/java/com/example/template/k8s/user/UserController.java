package com.example.template.k8s.user;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/kube/user")
public class UserController {

    @Autowired
    KafkaTemplate kafkaTemplate;

    @Autowired
    UserDetailRepository userDetailRepository;

    @Value("${topic.stateMsgTopic}")
    private String stateMsgTopic;

    @RequestMapping(value = "/getUserDetail", method = RequestMethod.GET)
    public UserDetail getUserDetail(HttpServletRequest request, @RequestParam String username) {
        //로그인한 사용자 ID 가져오기
        if( username != null) {
            System.out.println(username);

            Optional<UserDetail> userDetail = userDetailRepository.findById(username);

            return userDetail.orElse(null);
        }

        return null;
    }

    @RequestMapping(value = "/saveUserDetail", method = RequestMethod.PUT)
    public void saveUserDetails(HttpServletRequest request,
                                @RequestBody Map<String, Object> param ){
        if( param != null) {
            System.out.println(param.get("username"));
            System.out.println(param.get("host"));
            System.out.println(param.get("token"));

            UserDetail userDetail = new UserDetail();
            userDetail.setUsername((String)param.get("username"));
            userDetail.setHost((String)param.get("host"));
            userDetail.setToken((String)param.get("token"));

            userDetailRepository.save(userDetail);

            // 모니터링 서비스에게, 해당 host 의 데이터를 수집하라고 명령함.

            JSONObject data = new JSONObject();
            data.put("host", userDetail.getHost());
            data.put("token", userDetail.getToken());

            kafkaTemplate.send(new ProducerRecord<String, JSONObject>(stateMsgTopic, userDetail.getHost() , data));
        }

    }
}
