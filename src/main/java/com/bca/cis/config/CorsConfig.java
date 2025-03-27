package com.bca.cis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("https://cms-byc2024.kelolain.id", "http://localhost:8080", "http://localhost:8090") // Sesuaikan dengan URL aplikasi A
                        .allowedMethods("GET", "POST", "PUT", "DELETE") // Sesuaikan dengan metode HTTP yang dibutuhkan
                        .allowedHeaders("*") // Mengizinkan semua header
                        .allowCredentials(true);
            }
        };
    }
}
