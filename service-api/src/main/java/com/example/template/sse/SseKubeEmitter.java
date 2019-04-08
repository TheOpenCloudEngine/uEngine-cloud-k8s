package com.example.template.sse;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public class SseKubeEmitter extends SseEmitter {

    private String namespace;
    //    private String provider;
    private String instanceType;
    private String host;

//    public SseKubeEmitter(String name, String provider) {
//        super();
//        this.name = name;
//        this.provider = provider;
//    }

    public SseKubeEmitter(String instanceType, String namespace, String host) {
        super();
        this.instanceType = instanceType;
        this.namespace = namespace;
        this.host = host;
    }

    public String getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(String instanceType) {
        this.instanceType = instanceType;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
