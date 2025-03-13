package com.bca.cis.controller;

import com.bca.cis.response.ApiBcaResponse;
import com.bca.cis.response.ApiDataResponse;
import com.bca.cis.service.CheckCustomerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@Tag(name = "Check Customer")
//@SecurityRequirement(name = "Authorization")
@RequiredArgsConstructor
@Slf4j
public class CheckCustomerController {

    private final CheckCustomerService checkCustomerService;

    @Value("${test.jwtSecret}")
    private String authToken;

    @GetMapping("/eai/cis/data/v4/api/debit-cards/{card-number}")
    public ResponseEntity<?> DebitCardsChecks(
            @PathVariable("card-number") String cardNumber,
            @RequestParam(name = "function-code", defaultValue = "10") Integer functionCode,
            @RequestHeader(name = "Auth") String Authorization
    ) {

        try {
            if (!(functionCode == 02 || functionCode == 10)) {
                throw new RuntimeException("Error function code is not presents");
            }

            if (!Objects.equals(Authorization, authToken)) {
                throw new RuntimeException("No authorization with this token");
            }

            ApiBcaResponse.ErrorSchemaResponse response = new ApiBcaResponse.ErrorSchemaResponse();
            response.setError_code("EAI-000");
            response.setError_message(new ApiBcaResponse.ErrorMessageResponse("Success", "Berhasil"));
            return ResponseEntity.ok().body(new ApiBcaResponse(response, checkCustomerService.findDebitCards(cardNumber)));
        } catch (Exception e) {
            log.error("Error as : {}", e.getMessage(), e);
            ApiBcaResponse.ErrorSchemaResponse response = new ApiBcaResponse.ErrorSchemaResponse();
            response.setError_code("EAI-XXX");
            response.setError_message(new ApiBcaResponse.ErrorMessageResponse(e.getMessage(), e.getMessage()));
            return ResponseEntity.ok().body(new ApiDataResponse(false, e.getMessage(), response));
        }
    }

    @GetMapping("/eai/cis/data/v4/api/accounts/{application-code}/{account-number}")
    public ResponseEntity<?> AccountNumberChecks(
            @PathVariable("application-code") String applicationCode,
            @PathVariable("account-number") String accountNumber,
            @RequestParam(name = "function-code", defaultValue = "10") Integer functionCode,
            @RequestHeader(name = "Auth") String Authorization
    ) {

        try {
            if (!(functionCode == 02 || functionCode == 10)) {
                throw new RuntimeException("Error function code is not presents");
            }

            if (!Objects.equals(Authorization, authToken)) {
                throw new RuntimeException("No authorization with this token");
            }

            ApiBcaResponse.ErrorSchemaResponse response = new ApiBcaResponse.ErrorSchemaResponse();
            response.setError_code("EAI-000");
            response.setError_message(new ApiBcaResponse.ErrorMessageResponse("Success", "Berhasil"));
            return ResponseEntity.ok().body(new ApiBcaResponse(response, checkCustomerService.findAccountNumberChecks(applicationCode, accountNumber)));
        } catch (Exception e) {
            log.error("Error as : {}", e.getMessage(), e);
            ApiBcaResponse.ErrorSchemaResponse response = new ApiBcaResponse.ErrorSchemaResponse();
            response.setError_code("EAI-XXX");
            response.setError_message(new ApiBcaResponse.ErrorMessageResponse(e.getMessage(), e.getMessage()));
            return ResponseEntity.ok().body(new ApiDataResponse(false, e.getMessage(), response));
        }
    }

    @GetMapping("/eai/channel-products/api/customers")
    public ResponseEntity<?> RelationChecks(
            @RequestParam(name = "cis-custno ", defaultValue = "1112223334") String cisCustomerNumber,
            @RequestHeader(name = "Auth") String Authorization
    ) {

        try {
            if (!Objects.equals(Authorization, authToken)) {
                throw new RuntimeException("No authorization with this token");
            }

            ApiBcaResponse.ErrorSchemaResponse response = new ApiBcaResponse.ErrorSchemaResponse();
            response.setError_code("ESB-00-000");
            response.setError_message(new ApiBcaResponse.ErrorMessageResponse("Success", "Berhasil"));
            return ResponseEntity.ok().body(new ApiBcaResponse(response, checkCustomerService.findRelationChecks(cisCustomerNumber)));
        } catch (Exception e) {
            log.error("Error as : {}", e.getMessage(), e);
            ApiBcaResponse.ErrorSchemaResponse response = new ApiBcaResponse.ErrorSchemaResponse();
            response.setError_code("EAI-XXX");
            response.setError_message(new ApiBcaResponse.ErrorMessageResponse(e.getMessage(), e.getMessage()));
            return ResponseEntity.ok().body(new ApiDataResponse(false, e.getMessage(), response));
        }
    }


}
