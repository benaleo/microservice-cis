package com.bca.cis.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@SecurityScheme(
        name = "Authorization",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "JWT Authorization header using the Bearer scheme."
)
public class SwaggerConfig {


    @Value("${app.base.url}")
    private String baseUrl;

    @Bean
    public OpenAPI myOpenAPI() {
        Server appServer = new Server();
        appServer.setUrl(baseUrl);
        appServer.setDescription("Development Server");

        return new OpenAPI()
                .servers(List.of(appServer));
    }

//    @Bean
//    public GroupedOpenApi eaiApi() {
//        return GroupedOpenApi.builder()
//                .group("EAI_API")
//                .pathsToMatch("/eai/**")
//                .build();
//    }
//
//    @Bean
//    public GroupedOpenApi appsApi() {
//        return GroupedOpenApi.builder()
//                .group("Apps_API")
//                .pathsToMatch("/api/**")
//                .build();
//    }
//
//    @Bean
//    public GroupedOpenApi cmsApi() {
//        return GroupedOpenApi.builder()
//                .group("CMS_API")
//                .pathsToMatch("/cms/**")
//                .build();
//    }
}
