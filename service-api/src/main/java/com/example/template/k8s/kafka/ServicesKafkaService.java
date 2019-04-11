package com.example.template.k8s.kafka;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.example.template.k8s.service.Services;
import com.example.template.k8s.service.ServicesService;
import io.kubernetes.client.models.V1LoadBalancerIngress;
import io.kubernetes.client.models.V1Service;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.example.template.sse.SseBaseMessageHandler;
import com.google.gson.Gson;



@Service
public class ServicesKafkaService {

    private static final Logger LOG = LoggerFactory.getLogger(ServicesKafkaService.class);

    @Autowired
    private ServicesService servicesService;

    @Autowired
    private SseBaseMessageHandler messageHandler;


//    @KafkaListener(topics = "${topic.serviceMsgTopic}")
    public void listenByServicesOld(@Payload String message, ConsumerRecord<?, ?> consumerRecord) {

    	Gson gson = new Gson();
    	Services svs = gson.fromJson(message, Services.class);

		if("DELETED".equals(svs.getProvider())) {
    		servicesService.delete();
		} else {
			SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss.SSS");
			Calendar cal = Calendar.getInstance();
			String today = null;
			today = formatter.format(cal.getTime());
			Timestamp ts = Timestamp.valueOf(today);
			svs.setCreateTime(ts);


			servicesService.update(svs);
			messageHandler.publish("service", message, svs.getNamespace(), svs.getHost());
			System.out.println(message);
		}
    }

    @KafkaListener(topics = "${topic.serviceMsgTopic}")
    public void listenByServices(@Payload String message, ConsumerRecord<?, ?> consumerRecord) {

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

		Gson gson = new Gson();
		V1Service item = gson.fromJson(message, V1Service.class);
		String namespace = item.getMetadata().getNamespace();
		String name = item.getMetadata().getName();

		Services svs = new Services();
		svs.setProvider("K8S");
		svs.setName(item.getMetadata().getName());
		svs.setNamespace(item.getMetadata().getNamespace());

		if("DELETED".equals(status)) {
			servicesService.delete(host, namespace, name);
			svs.setApiVersion(status);
		}else {

			svs.setId(item.getMetadata().getUid());
			svs.setHost(host);
			svs.setApiVersion(item.getApiVersion());
			svs.setKind(item.getKind());

			// service의 메타데이터 정보
			{
				svs.setCreateTimeStamp(createTimeStamp);

			}

			// service의 spec 정보
			{
				svs.setSpecSessionAffinity(item.getSpec().getSessionAffinity());
				svs.setSpecPorts(new Gson().toJson(item.getSpec().getPorts()));
				svs.setSpecClusterIp(item.getSpec().getClusterIP());
				svs.setSpecType(item.getSpec().getType());

			}

			// service의 status 정보
			{
				if (item.getStatus().getLoadBalancer() != null && item.getStatus().getLoadBalancer().getIngress() != null) {
					for (V1LoadBalancerIngress v1Ingress : item.getStatus().getLoadBalancer().getIngress()) {
						svs.setHostname(v1Ingress.getHostname());
						svs.setIngressIp(v1Ingress.getIp());
					}
				}
			}

			{
				svs.setSourceData(new Gson().toJson(item));
			}

			{
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
				Calendar cal = Calendar.getInstance();
				String today = null;
				today = formatter.format(cal.getTime());
				Timestamp ts = Timestamp.valueOf(today);
				svs.setCreateTime(ts);
			}

			servicesService.update(svs);
		}
		String json = gson.toJson(svs);
		messageHandler.publish("service", json, svs.getNamespace(), host);
    }
}
