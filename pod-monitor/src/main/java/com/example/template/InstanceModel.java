package com.example.template;

import lombok.Data;

import java.io.Serializable;
import java.util.Properties;

@Data
public class InstanceModel implements Serializable {

    String id;
    String name;
    String provider;
    String type;
    String status;

    String createDate;
    String regDate;

    String properties;

    // 인스턴스 상태값 - DB 에서는 제외
    InstanceState instanceState;

}

//provider: "AWS", type: "EC2-t2mid", id: "XXXX", created: "2019-1-1..",
//         properties:{
//         external-ip: "…..",
//         vlan: "…..",
//         …
//provider: "K8S", type: "pod", id: "YYYY", created: "2019-1-1..",
// properties:{
// running-container: "1/2",
// node: "node1",
// …
