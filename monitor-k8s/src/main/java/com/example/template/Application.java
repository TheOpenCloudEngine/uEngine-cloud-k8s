package com.example.template;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Application {

    protected static ApplicationContext applicationContext;
    public static void main(String[] args) {
        applicationContext = SpringApplication.run(Application.class, args);
    }

//    public static void restart() {
//        ApplicationArguments args = context.getBean(ApplicationArguments.class);
// 
//        Thread thread = new Thread(() -> {
//            context.close();
//            context = SpringApplication.run(Application.class, args.getSourceArgs());
//        });
// 
//        thread.setDaemon(false);
//        thread.start();
//    }
}

