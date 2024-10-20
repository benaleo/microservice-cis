package com.bca.cis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
public class AppConfig {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public SecretKey SECRET_KEY() {
        return new SecretKeySpec(jwtSecret.getBytes(), 0, jwtSecret.getBytes().length, "AES");
    }


}
