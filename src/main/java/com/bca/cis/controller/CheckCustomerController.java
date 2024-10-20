package com.bca.cis.controller;

import com.bca.cis.response.ApiBcaResponse;
import com.bca.cis.response.ApiDataResponse;
import com.bca.cis.response.ApiResponse;
import com.bca.cis.service.CheckCustomerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Data
@AllArgsConstructor
@Tag(name = "Check Customer")
//@SecurityRequirement(name = "Authorization")
@RequestMapping("/eai/cis/data/v4/api/")
public class CheckCustomerController {

    private final CheckCustomerService checkCustomerService;

    @GetMapping("/debit-cards/{card-number}")
    public ResponseEntity<?> DebitCardsChecks(@PathVariable("card-number") String cardNumber) {

        try{
            ApiBcaResponse.ErrorSchemaResponse errorSchemaResponse = new ApiBcaResponse.ErrorSchemaResponse();
            errorSchemaResponse.setError_code("EAI-000");
            errorSchemaResponse.setError_message(new ApiBcaResponse.ErrorMessageResponse("Success", "Berhasil"));
            return ResponseEntity.ok().body(new ApiBcaResponse(errorSchemaResponse, checkCustomerService.findDebitCards(cardNumber)));
        }catch (Exception e){
            ApiBcaResponse.ErrorSchemaResponse errorSchemaResponse = new ApiBcaResponse.ErrorSchemaResponse();
            errorSchemaResponse.setError_code("EAI-XXX");
            errorSchemaResponse.setError_message(new ApiBcaResponse.ErrorMessageResponse(e.getMessage(), e.getMessage()));
            return ResponseEntity.ok().body(new ApiResponse(false, e.getMessage()));
        }
    }

     @GetMapping("/accounts/{application-code}/{account-number}")
    public ResponseEntity<?> AccountNumberChecks(@PathVariable("application-code") String applicationCode, @PathVariable("account-number") String accountNumber) {

        return null;
    }


}
