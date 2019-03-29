package com.example.template;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.security.KeyPair;

@Configuration
@EnableWebFluxSecurity
public class ResourceServerConfiguration {

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) throws Exception {
        http
                .authorizeExchange()
                .pathMatchers("/.well-known/jwks.json")
                .permitAll();

        http
                .authorizeExchange()
                .pathMatchers("/oauth/**","/login/**").permitAll()
                .anyExchange().authenticated()
                .and()
                .oauth2ResourceServer()
                .jwt()
                ;

        return http.build();
    }

    @Bean
    public KeyPair makeKeyPair(){
        KeyPair keyPair = new KeyStoreKeyFactory(
                new ClassPathResource("server.jks"), "qweqwe".toCharArray())
                .getKeyPair("uengine", "qweqwe".toCharArray());
        return keyPair;
    }

//    @Override
//    public void configure(final HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers("/oauth/**","/login/**")
//                .permitAll()
//                .antMatchers("/**")
//                .authenticated();
//    }

}
