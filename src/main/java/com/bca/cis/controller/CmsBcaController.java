package com.bca.cis.controller;

import com.bca.cis.response.ApiBcaResponse;
import com.bca.cis.response.ApiDataResponse;
import com.bca.cis.service.BCAApiService;
import com.bca.cis.service.CheckCustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CmsBcaController {

    private final BCAApiService bcaApiService;
    private final CheckCustomerService checkCustomerService;

    @Value("${test.jwtSecret}")
    private String authToken;

    @GetMapping("/cms/fetch-bca-data")
    public Mono<String> fetchBcaData() {
        return bcaApiService.getBCAData();
    }

    @GetMapping("/cms/debit-cards/api/account-status/card-number/{card-number}")
    public ResponseEntity<?> DebitCardsChecks(
            @PathVariable("card-number") String cardNumber,
            @RequestHeader(name = "Auth") String Authorization
    ) {

        try {
            if (!Objects.equals(Authorization, authToken)) {
                throw new RuntimeException("No authorization with this token");
            }

            ApiBcaResponse.ErrorSchemaResponse response = new ApiBcaResponse.ErrorSchemaResponse();
            response.setError_code("MC");
            response.setError_message(new ApiBcaResponse.ErrorMessageResponse("INQUIRY SUCCESSFULLY", "INQUIRY BERHASIL"));
            return ResponseEntity.ok().body(new ApiBcaResponse(response, checkCustomerService.findAcctFactByNumber(cardNumber)));
        } catch (Exception e) {
            log.error("Error as : {}", e.getMessage(), e);
            ApiBcaResponse.ErrorSchemaResponse response = new ApiBcaResponse.ErrorSchemaResponse();
            response.setError_code("MTV");
            response.setError_message(new ApiBcaResponse.ErrorMessageResponse(e.getMessage(), e.getMessage()));
            return ResponseEntity.ok().body(new ApiDataResponse(false, e.getMessage(), response));
        }
    }
}
