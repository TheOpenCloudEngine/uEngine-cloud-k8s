package com.example.template.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@Profile({"default"})
public class ConfigurationLocal implements WebMvcConfigurer {

    /**
     * 로컬에서 gateway 를 안키고 작업을 할때는 cors 가 문제가 되니..
     * 아래 부분을 주석 풀고 작업을 하면 됨
     */
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("*")
//                .allowedMethods("POST", "GET", "DELETE", "PUT", "HEAD")
//                .allowCredentials(false);
//    }

}
