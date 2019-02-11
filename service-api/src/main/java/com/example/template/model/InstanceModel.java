package com.example.template.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Properties;

@Data
@Entity
@Table(name = "INSTANCE_VIEW")
public class InstanceModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    String id;
    String name;
    String provider;
    String type;
    String status;

    String createDate;
    String regDate;

    String properties;

    // 인스턴스 상태값 - DB 에서는 제외
    @Transient
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
