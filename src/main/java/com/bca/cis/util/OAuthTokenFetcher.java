package com.bca.cis.util;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.util.Base64;

public class OAuthTokenFetcher {
    public static void main(String[] args) {
        String clientId = "7ea10ff9";
        String clientSecret = "f2305c5424aa17a858b34e2283554184";
        String auth = clientId + ":" + clientSecret;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        String url = "https://sso-api-bca/auth/realms/3scaledev/protocol/openid-connect/token";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + encodedAuth);
        headers.set("Content-Type", "application/x-www-form-urlencoded");

        HttpEntity<String> entity = new HttpEntity<>("grant_type=client_credentials", headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        System.out.println("Response: " + response.getBody());
    }
}
