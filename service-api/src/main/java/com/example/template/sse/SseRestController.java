package com.example.template.sse;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Controller
//@CrossOrigin(origins = "*")
@RequestMapping("/kubesse")
public class SseRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SseRestController.class);

    private static final CopyOnWriteArrayList<SseKubeEmitter> userBaseEmitters = new CopyOnWriteArrayList<>();

    public String nameSpace = null;

    @CrossOrigin(origins = "*")
    @GetMapping("/")
    public SseEmitter getNewKube(HttpServletRequest request,
                                 HttpServletResponse response,
                                 @RequestParam(value = "instanceType", required = false) String instanceType,
                                 @RequestParam(value = "namespace", required = false) String namespace

    ) {
        SseKubeEmitter emitter = new SseKubeEmitter(instanceType, namespace);
        userBaseEmitters.add(emitter);

        emitter.onCompletion(() -> this.userBaseEmitters.remove(emitter));
        emitter.onTimeout(() -> {
            this.userBaseEmitters.remove(emitter);
        });

        return emitter;
    }

    @EventListener
    public void onKubeSse(SseBaseMessage appEntityBaseMessage) {
        LOGGER.info("appEntityBaseMessage");
        System.out.println(this.userBaseEmitters.size());
        List<SseEmitter> deadEmitters = new ArrayList<>();
        this.userBaseEmitters.forEach(emitter -> {
            try {
                /*
                    todo : nameSpace 조건 부분
                 */
                LOGGER.info("appEntityBaseMessage");

                if (appEntityBaseMessage.getInstanceType().equals(emitter.getInstanceType())) {
                    if (emitter.getNamespace() == null) {
                        emitter.send(appEntityBaseMessage);
                    } else if (appEntityBaseMessage.getNamespace().equals(emitter.getNamespace())) {
                        emitter.send(appEntityBaseMessage);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.info("dead");
                deadEmitters.add(emitter);
                this.userBaseEmitters.remove(emitter);
            }
        });
        this.userBaseEmitters.remove(deadEmitters);
    }
}
