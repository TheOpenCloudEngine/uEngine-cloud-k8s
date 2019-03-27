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
    public void getUserDetail(HttpServletRequest request, @RequestParam String username) {
        //로그인한 사용자 ID 가져오기
        if( username != null) {
            System.out.println(username);
        }

        String host = "https://api.k8s.bzdvops.com";
        String token = "eyJhbGciOiJSUzI1NiIsImtpZCI6IiJ9.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJkZWZhdWx0Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZWNyZXQubmFtZSI6ImNsb3VkdXNlci10b2tlbi16cmtqaiIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50Lm5hbWUiOiJjbG91ZHVzZXIiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC51aWQiOiI0ZmJmNzk0YS0zNTgwLTExZTktYTU2OC0wMjkxMGMyMWIzOTgiLCJzdWIiOiJzeXN0ZW06c2VydmljZWFjY291bnQ6ZGVmYXVsdDpjbG91ZHVzZXIifQ.APncfC7biCYEre4LZ3S-TVcf641qpQlo7r_BN0khN8ovnT7rR3DWGTaUDLP2eFQBLUvEVSAgTT1g0wF1OFsqEx-Sn3fHByyf1r8t15wvN_XJFM2_V_ZZBosUeZCciklcky0jwF6AWcSpUo9nKa23yBtylJ9d6EPjAq8KtURdX7IVb5i8j0InSExyOQZv5xJ-yv55GB_yRrI9rQ6cwxt_PdFaQiFLjSjnp6SvZj3nPACw_qb98w2I4p_O8DZ5SE-b4OetZj0xmZM7ELXBbceMDepT0UHrU1ZcIc54aWNnhyGFdxspxwrGWSDtNL4T6KKbTqU6HVXkiJTKCw1w9E9hHg";

        JSONObject data = new JSONObject();
        data.put("host", host);
        data.put("token", token);

        kafkaTemplate.send(new ProducerRecord<String, JSONObject>(stateMsgTopic, host , data));


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


            String host = "https://api.k8s.bzdvops.com";
            String token = "eyJhbGciOiJSUzI1NiIsImtpZCI6IiJ9.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJkZWZhdWx0Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZWNyZXQubmFtZSI6ImNsb3VkdXNlci10b2tlbi16cmtqaiIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50Lm5hbWUiOiJjbG91ZHVzZXIiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC51aWQiOiI0ZmJmNzk0YS0zNTgwLTExZTktYTU2OC0wMjkxMGMyMWIzOTgiLCJzdWIiOiJzeXN0ZW06c2VydmljZWFjY291bnQ6ZGVmYXVsdDpjbG91ZHVzZXIifQ.APncfC7biCYEre4LZ3S-TVcf641qpQlo7r_BN0khN8ovnT7rR3DWGTaUDLP2eFQBLUvEVSAgTT1g0wF1OFsqEx-Sn3fHByyf1r8t15wvN_XJFM2_V_ZZBosUeZCciklcky0jwF6AWcSpUo9nKa23yBtylJ9d6EPjAq8KtURdX7IVb5i8j0InSExyOQZv5xJ-yv55GB_yRrI9rQ6cwxt_PdFaQiFLjSjnp6SvZj3nPACw_qb98w2I4p_O8DZ5SE-b4OetZj0xmZM7ELXBbceMDepT0UHrU1ZcIc54aWNnhyGFdxspxwrGWSDtNL4T6KKbTqU6HVXkiJTKCw1w9E9hHg";

            JSONObject data = new JSONObject();
            data.put("host", host);
            data.put("token", token);

            kafkaTemplate.send(new ProducerRecord<String, JSONObject>(stateMsgTopic, host , data));
        }

    }
}
