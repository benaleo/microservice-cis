package com.bca.cis.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class OAuthService {

    private final WebClient webClient;

    // Inject the WebClient Bean
    public OAuthService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://sso-api-bca/auth/realms/3scaledev/protocol/openid-connect").build();
    }

    @Value("${oauth.clientId}")
    private String clientId;

    @Value("${oauth.clientSecret}")
    private String clientSecret;

    public Mono<String> getAccessToken() {
        String clientCredentials = clientId + ":" + clientSecret;
        String authorizationHeader = "Basic " + java.util.Base64.getEncoder().encodeToString(clientCredentials.getBytes());

        return webClient.post()
                .uri("/token")
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .bodyValue("grant_type=client_credentials")
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    // You can parse the access token from the response (for example, with Jackson)
                    // For now, we're returning the raw response, which will be a JSON with the access token
                    System.out.println(response);  // Logging response for debugging
                    return response;
                });
    }
}
