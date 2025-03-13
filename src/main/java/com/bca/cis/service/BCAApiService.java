package com.bca.cis.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public class BCAApiService {

    private final WebClient webClient;
    private final OAuthService oauthService;

    // Inject OAuthService to get token
    public BCAApiService(WebClient.Builder webClientBuilder, OAuthService oauthService) {
        this.webClient = webClientBuilder.baseUrl("https://api-bca.com").build();
        this.oauthService = oauthService;
    }

    public Mono<String> getBCAData() {
        return oauthService.getAccessToken()
                .flatMap(tokenResponse -> {
                    // Extract the access token from the response
                    String accessToken = extractAccessToken(tokenResponse);

                    return webClient.get()
                            .uri("/api/endpoint")  // Replace with actual BCA API endpoint
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                            .retrieve()
                            .bodyToMono(String.class);
                });
    }

    private String extractAccessToken(String response) {
        // A simple way to extract the access token from the JSON response
        // (You can use a JSON parser library like Jackson or Gson)
        // Example: response might be {"access_token": "YOUR_TOKEN", ...}
        int start = response.indexOf("access_token\":\"") + 15;
        int end = response.indexOf("\"", start);
        return response.substring(start, end);
    }

    public Object getTokenBCA(String clientID, String clientSecret) {

        if (clientID == null || clientSecret == null) {
            Map<String, String> err = new HashMap<>();
            err.put("error", "Error name");
            err.put("error_description", "error brief description");
            return ResponseEntity.badRequest().body(err);
        }

        Map<String, String> response = new HashMap<>();
        response.put("access_token", "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJNZnBVeC15RkFJU2lsQ3pwRFNFYktVbHFtT09zRjZBQV84UnV4MXNIM1VzIn0.eyJleHAiOjE3MjUzNTU5NTksImlhdCI6MTcyNTM1MjM1OSwianRpIjoiZTViNmQxZDMtZDNkOS00OWYwLWE0NGItM2U2YTZiYWM2MWQ1IiwiaXNzIjoiaHR0cHM6Ly9zc28tYXBpZ3ctaW50LmR0aS5jby5pZC9yZWFsbXMvM3NjYWxlLWRldiIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiI5ZGJmNmFjMi1lY2RlLTQ5MTEtODRhOC1hNGZkYThkYzRjNTEiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiI3ZWExMGZmOSIsImFjciI6IjEiLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsib2ZmbGluZV9hY2Nlc3MiLCJkZWZhdWx0LXJvbGVzLTNzY2FsZS1kZXYtbmV3YXJjIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6ImVtYWlsIHByb2ZpbGUiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsImNsaWVudElkIjoiN2VhMTBmZjkiLCJjbGllbnRIb3N0IjoiMTAuNTguODAuNSIsInByZWZlcnJlZF91c2VybmFtZSI6InNlcnZpY2UtYWNjb3VudC03ZWExMGZmOSIsImNsaWVudEFkZHJlc3MiOiIxMC41OC44MC41In0.bo4Ozj5Mq4cSiIwCCeBjFP5_mRe1Vn6tked0MWzlQo8ugnazLT5LnHVaAOZx9CYa2y0f6Zjhcisog5djOIpABlk2nbL22nsHJgpOwShqK1-mLLrgIWsCjZSpBivdSOFGj5W3oZZtWUIaXmeoLX3i9lg342Ge_lAxcUYbexW7qBKpG9pgiv3WtX66lMNnrTM8Ctl69EN4vk5g4UXRHs8hf6FW13r4tcXa9ggp_FmR5klC638A4etUGxz3G9WKIZRDqv2-gFTgTjRP8pRipL6mOdASRZuC1vTn0suGT4ebg9grKUjTApQ5kLNcJv6SjMS_HjJy9Hn_eW8FTmCC6GA5B0a83sKNryMEAjkSRymaLjLnXQW1TR3W_5n6W3sJUcCnxxJoL6fqSYByynQQKLXBvOUepFKuwnmjM4HLV5xz01FeU3j9sr8K1lln9w8vf4xV0n0zY76UCv7XcV7UAJQJ7VNhvBKyapOz5TAQFAulNfO3UfxqL4kNK2iW-elcm9heqTLSScqyueFrpb5IgAiY7p5PmTu0yZGtiMaGdz6VT0WpBOLYFVwaMa1rLVrqp92VyzCostzgKARMcsv-UlFFstZrx6mUcq1g_9Xez95gWlAaJCjvfL9JHI--TfzZlhzvWWP5cI13JkJ44Foy3igTJG2noFy9uf6CVrqXrqgs");
        response.put("expires_in", "3600");
        response.put("refresh_expires_in", "0");
        response.put("token_type", "Bearer");
        response.put("not-before-policy", "0");
        response.put("scope", "email profile");
        return response;
    }


}
