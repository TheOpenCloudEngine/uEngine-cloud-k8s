package com.example.template.emitter;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public class KubeEmitter extends SseEmitter {

    private String name;
    private String provider;

    public KubeEmitter(String name, String provider) {
        super();
        this.name = name;
        this.provider = provider;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
