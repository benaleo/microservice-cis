package com.bca.cis.controller;

import com.bca.cis.service.BCAApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class ApiBcaController {

    @Autowired
    private BCAApiService bcaApiService;

    @GetMapping("/api/fetch-bca-data")
    public Mono<String> fetchBcaData() {
        return bcaApiService.getBCAData();
    }

    @PostMapping("/auth/realms/3scale-dev/protocol/openid-connect/token")
    public ResponseEntity<?> getTokenSSO(@RequestHeader(required = false) String clientID, @RequestHeader(required = false) String clientSecret){

        try{
            Object response = bcaApiService.getTokenBCA(clientID, clientSecret);
            return ResponseEntity.ok().body(response);
        }catch (Exception e){
            log.error("Error get token from send request post");
            return ResponseEntity.badRequest().body("Error");
        }

    }
}
