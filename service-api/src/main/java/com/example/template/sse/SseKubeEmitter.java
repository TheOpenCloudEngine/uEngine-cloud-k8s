package com.example.template.sse;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public class SseKubeEmitter extends SseEmitter {

//    private String name;
//    private String provider;
    private String instanceType;

//    public SseKubeEmitter(String name, String provider) {
//        super();
//        this.name = name;
//        this.provider = provider;
//    }
    
    public SseKubeEmitter(String instanceType) {
        super();
        this.instanceType = instanceType;
    }

	public String getInstanceType() {
		return instanceType;
	}

	public void setInstanceType(String instanceType) {
		this.instanceType = instanceType;
	}

    
}
