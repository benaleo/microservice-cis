package com.bca.cis.util;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@RequiredArgsConstructor
public class authBca {

    private static final String KEYCLOAK_REALM = "BCA";
    private static final String KEYCLOAK_GRANT_TYPE = "client_credentials";
    private static final String KEYCLOAK_HOST = "https://sso-api-bca/";

    private final RestTemplate restTemplate;

    private String obtainToken(String clientId, String clientSecret) {
        String base64 = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", KEYCLOAK_GRANT_TYPE);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = restTemplate.exchange(KEYCLOAK_HOST + "auth/realms/" + KEYCLOAK_REALM + "/protocol/openid-connect/token", HttpMethod.POST, request, String.class);
        return response.getBody();
    }

}
