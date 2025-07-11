package com.bca.cis.controller;

import com.bca.cis.model.OtpGenerateSingleRequest;
import com.bca.cis.model.OtpGenerateVerifyRequest;
import com.bca.cis.response.ApiBcaResponse;
import com.bca.cis.response.ApiDataResponse;
import com.bca.cis.service.BCAApiService;
import com.bca.cis.service.CheckCustomerService;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "check eai v3.0", description = "check eai v3.0")
public class CmsBcaController {

    private final BCAApiService bcaApiService;
    private final CheckCustomerService checkCustomerService;

    @Value("${test.jwtSecret}")
    private String authToken;

    @GetMapping("/cms/debit-cards/api/account-status/card-number/{card-number}")
    public ResponseEntity<?> DebitCardsChecks(
            @PathVariable("card-number") String cardNumber,
            @RequestHeader(name = "Authorization") String Authorization) {

        try {
            if (!Objects.equals(Authorization, authToken)) {
                throw new RuntimeException("No authorization with this token");
            }

            ApiBcaResponse.ErrorSchemaResponse response = new ApiBcaResponse.ErrorSchemaResponse();
            response.setError_code("MC");
            response.setError_message(
                    new ApiBcaResponse.ErrorMessageResponse("INQUIRY SUCCESSFULLY", "INQUIRY BERHASIL"));
            return ResponseEntity.ok()
                    .body(new ApiBcaResponse(response, checkCustomerService.findAcctFactByNumber(cardNumber)));
        } catch (Exception e) {
            log.error("Error as : {}", e.getMessage(), e);
            ApiBcaResponse.ErrorSchemaResponse response = new ApiBcaResponse.ErrorSchemaResponse();
            response.setError_code("MTV");
            response.setError_message(new ApiBcaResponse.ErrorMessageResponse(e.getMessage(), e.getMessage()));
            return ResponseEntity.ok().body(new ApiDataResponse(false, e.getMessage(), response));
        }
    }

    @PostMapping("/cis-gen-2/customers/api")
    public ResponseEntity<?> InquiryCis(
            @RequestHeader(name = "Authorization", defaultValue = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJNZnBVeC15RkFJU2lsQ3pwRFNFYktVbHFtT09zRjZBQV84UnV4MXNIM1VzIn0.eyJleHAiOjE3MjUzNTU5NTksImlhdCI6MTcyNTM1MjM1OSwianRpIjoiZTViNmQxZDMtZDNkOS00OWYwLWE0NGItM2U2YTZiYWM2MWQ1IiwiaXNzIjoiaHR0cHM6Ly9zc28tYXBpZ3ctaW50LmR0aS5jby5pZC9yZWFsbXMvM3NjYWxlLWRldiIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiI5ZGJmNmFjMi1lY2RlLTQ5MTEtODRhOC1hNGZkYThkYzRjNTEiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiI3ZWExMGZmOSIsImFjciI6IjEiLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsib2ZmbGluZV9hY2Nlc3MiLCJkZWZhdWx0LXJvbGVzLTNzY2FsZS1kZXYtbmV3YXJjIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6ImVtYWlsIHByb2ZpbGUiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsImNsaWVudElkIjoiN2VhMTBmZjkiLCJjbGllbnRIb3N0IjoiMTAuNTguODAuNSIsInByZWZlcnJlZF91c2VybmFtZSI6InNlcnZpY2UtYWNjb3VudC03ZWExMGZmOSIsImNsaWVudEFkZHJlc3MiOiIxMC41OC44MC41In0.bo4Ozj5Mq4cSiIwCCeBjFP5_mRe1Vn6tked0MWzlQo8ugnazLT5LnHVaAOZx9CYa2y0f6Zjhcisog5djOIpABlk2nbL22nsHJgpOwShqK1-mLLrgIWsCjZSpBivdSOFGj5W3oZZtWUIaXmeoLX3i9lg342Ge_lAxcUYbexW7qBKpG9pgiv3WtX66lMNnrTM8Ctl69EN4vk5g4UXRHs8hf6FW13r4tcXa9ggp_FmR5klC638A4etUGxz3G9WKIZRDqv2-gFTgTjRP8pRipL6mOdASRZuC1vTn0suGT4ebg9grKUjTApQ5kLNcJv6SjMS_HjJy9Hn_eW8FTmCC6GA5B0a83sKNryMEAjkSRymaLjLnXQW1TR3W_5n6W3sJUcCnxxJoL6fqSYByynQQKLXBvOUepFKuwnmjM4HLV5xz01FeU3j9sr8K1lln9w8vf4xV0n0zY76UCv7XcV7UAJQJ7VNhvBKyapOz5TAQFAulNfO3UfxqL4kNK2iW-elcm9heqTLSScqyueFrpb5IgAiY7p5PmTu0yZGtiMaGdz6VT0WpBOLYFVwaMa1rLVrqp92VyzCostzgKARMcsv-UlFFstZrx6mUcq1g_9Xez95gWlAaJCjvfL9JHI--TfzZlhzvWWP5cI13JkJ44Foy3igTJG2noFy9uf6CVrqXrqgs") String auth,
            @RequestHeader(name = "channel-user-id", defaultValue = "1112223334") String channelUserId,
            @RequestHeader(name = "channel-txn-id", defaultValue = "BYC-123") String channelTxnId,
            @RequestHeader(name = "reason-code", defaultValue = "28") String reasonCode,
            @RequestHeader(name = "operator", defaultValue = "operator") String operator,
            @RequestBody(required = false) String requestBody) {

        try {
            // Validate authorization token
            if (!Objects.equals(auth, authToken)) {
                log.error("Unauthorized access: Invalid Auth token");
                throw new RuntimeException("No authorization with this token");
            }

            String query = requestBody;
            log.info("query is {}", query);

            // Get accountNo from getByAccountNo on query
            String accountNo = null;
            try {
                String getByAccountNo = query.substring(query.indexOf("accountNo:") + 13);
                accountNo = getByAccountNo.substring(0, getByAccountNo.indexOf('"', 1));
                accountNo = accountNo.replaceAll("\\\\", "");
            } catch (StringIndexOutOfBoundsException e) {
                log.error("Error getting accountNo from query: {}", e.getMessage(), e);
            }
            log.info("accountNo is {}", accountNo);

            // Call the service and return the response
            ApiBcaResponse.ErrorSchemaResponse response = new ApiBcaResponse.ErrorSchemaResponse();
            response.setError_code("CNG-200-000");
            response.setError_message(
                    new ApiBcaResponse.ErrorMessageResponse("Success", "Success"));
            return ResponseEntity.ok()
                    .body(new ApiBcaResponse(response, checkCustomerService.findInquiryCisRelations(accountNo)));
        } catch (Exception e) {
            log.error("Error processing request: {}", e.getMessage(), e);
            ApiBcaResponse.ErrorSchemaResponse response = new ApiBcaResponse.ErrorSchemaResponse();
            response.setError_code("CNG-200-000");
            response.setError_message(new ApiBcaResponse.ErrorMessageResponse(e.getMessage(), e.getMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiDataResponse(false, e.getMessage(), response));
        }
    }

    @PostMapping("/midtier/ebanking/v2/api/mobile-number")
    public ResponseEntity<?> InquiryMobileNumber(
            @RequestHeader(name = "Authorization", defaultValue = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJNZnBVeC15RkFJU2lsQ3pwRFNFYktVbHFtT09zRjZBQV84UnV4MXNIM1VzIn0.eyJleHAiOjE3MjUzNTU5NTksImlhdCI6MTcyNTM1MjM1OSwianRpIjoiZTViNmQxZDMtZDNkOS00OWYwLWE0NGItM2U2YTZiYWM2MWQ1IiwiaXNzIjoiaHR0cHM6Ly9zc28tYXBpZ3ctaW50LmR0aS5jby5pZC9yZWFsbXMvM3NjYWxlLWRldiIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiI5ZGJmNmFjMi1lY2RlLTQ5MTEtODRhOC1hNGZkYThkYzRjNTEiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiI3ZWExMGZmOSIsImFjciI6IjEiLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsib2ZmbGluZV9hY2Nlc3MiLCJkZWZhdWx0LXJvbGVzLTNzY2FsZS1kZXYtbmV3YXJjIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6ImVtYWlsIHByb2ZpbGUiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsImNsaWVudElkIjoiN2VhMTBmZjkiLCJjbGllbnRIb3N0IjoiMTAuNTguODAuNSIsInByZWZlcnJlZF91c2VybmFtZSI6InNlcnZpY2UtYWNjb3VudC03ZWExMGZmOSIsImNsaWVudEFkZHJlc3MiOiIxMC41OC44MC41In0.bo4Ozj5Mq4cSiIwCCeBjFP5_mRe1Vn6tked0MWzlQo8ugnazLT5LnHVaAOZx9CYa2y0f6Zjhcisog5djOIpABlk2nbL22nsHJgpOwShqK1-mLLrgIWsCjZSpBivdSOFGj5W3oZZtWUIaXmeoLX3i9lg342Ge_lAxcUYbexW7qBKpG9pgiv3WtX66lMNnrTM8Ctl69EN4vk5g4UXRHs8hf6FW13r4tcXa9ggp_FmR5klC638A4etUGxz3G9WKIZRDqv2-gFTgTjRP8pRipL6mOdASRZuC1vTn0suGT4ebg9grKUjTApQ5kLNcJv6SjMS_HjJy9Hn_eW8FTmCC6GA5B0a83sKNryMEAjkSRymaLjLnXQW1TR3W_5n6W3sJUcCnxxJoL6fqSYByynQQKLXBvOUepFKuwnmjM4HLV5xz01FeU3j9sr8K1lln9w8vf4xV0n0zY76UCv7XcV7UAJQJ7VNhvBKyapOz5TAQFAulNfO3UfxqL4kNK2iW-elcm9heqTLSScqyueFrpb5IgAiY7p5PmTu0yZGtiMaGdz6VT0WpBOLYFVwaMa1rLVrqp92VyzCostzgKARMcsv-UlFFstZrx6mUcq1g_9Xez95gWlAaJCjvfL9JHI--TfzZlhzvWWP5cI13JkJ44Foy3igTJG2noFy9uf6CVrqXrqgs") String auth,
            @RequestHeader(name = "user-id", defaultValue = "QVBJREEtQkNBWUMtREVW") String channelUserId,
            @RequestParam(name = "customer-number", defaultValue = "10002000") String customerNumber
    ) {

        try {
            // Validate authorization token
            if (!Objects.equals(auth, authToken)) {
                log.error("Unauthorized access: Invalid Auth token");
                throw new RuntimeException("No authorization with this token");
            }


            // Call the service and return the response
            ApiBcaResponse.ErrorSchemaResponse response = new ApiBcaResponse.ErrorSchemaResponse();
            response.setError_code("MC");
            response.setError_message(
                    new ApiBcaResponse.ErrorMessageResponse("INQUIRY SUCCESSFULLY", "INQUIRY BERHASIL"));
            return ResponseEntity.ok()
                    .body(new ApiBcaResponse(response, checkCustomerService.findInquiryMobileNumber(customerNumber)));
        } catch (Exception e) {
            log.error("Error processing request: {}", e.getMessage(), e);
            ApiBcaResponse.ErrorSchemaResponse response = new ApiBcaResponse.ErrorSchemaResponse();
            response.setError_code("MTV");
            response.setError_message(new ApiBcaResponse.ErrorMessageResponse(e.getMessage(), e.getMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiDataResponse(false, e.getMessage(), response));
        }
    }

    @PostMapping("/eai/otp/v3/api/{product}/{pan}")
    public ResponseEntity<?> OtpGenerateSingle(
            @Schema(defaultValue = "byc_registration_sms", description = "product")
            @PathVariable String product,
            @Schema(defaultValue = "0810123456789", description = "pan")
            @PathVariable String pan,
            @RequestHeader(name = "Authorization", defaultValue = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJNZnBVeC15RkFJU2lsQ3pwRFNFYktVbHFtT09zRjZBQV84UnV4MXNIM1VzIn0.eyJleHAiOjE3MjUzNTU5NTksImlhdCI6MTcyNTM1MjM1OSwianRpIjoiZTViNmQxZDMtZDNkOS00OWYwLWE0NGItM2U2YTZiYWM2MWQ1IiwiaXNzIjoiaHR0cHM6Ly9zc28tYXBpZ3ctaW50LmR0aS5jby5pZC9yZWFsbXMvM3NjYWxlLWRldiIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiI5ZGJmNmFjMi1lY2RlLTQ5MTEtODRhOC1hNGZkYThkYzRjNTEiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiI3ZWExMGZmOSIsImFjciI6IjEiLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsib2ZmbGluZV9hY2Nlc3MiLCJkZWZhdWx0LXJvbGVzLTNzY2FsZS1kZXYtbmV3YXJjIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6ImVtYWlsIHByb2ZpbGUiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsImNsaWVudElkIjoiN2VhMTBmZjkiLCJjbGllbnRIb3N0IjoiMTAuNTguODAuNSIsInByZWZlcnJlZF91c2VybmFtZSI6InNlcnZpY2UtYWNjb3VudC03ZWExMGZmOSIsImNsaWVudEFkZHJlc3MiOiIxMC41OC44MC41In0.bo4Ozj5Mq4cSiIwCCeBjFP5_mRe1Vn6tked0MWzlQo8ugnazLT5LnHVaAOZx9CYa2y0f6Zjhcisog5djOIpABlk2nbL22nsHJgpOwShqK1-mLLrgIWsCjZSpBivdSOFGj5W3oZZtWUIaXmeoLX3i9lg342Ge_lAxcUYbexW7qBKpG9pgiv3WtX66lMNnrTM8Ctl69EN4vk5g4UXRHs8hf6FW13r4tcXa9ggp_FmR5klC638A4etUGxz3G9WKIZRDqv2-gFTgTjRP8pRipL6mOdASRZuC1vTn0suGT4ebg9grKUjTApQ5kLNcJv6SjMS_HjJy9Hn_eW8FTmCC6GA5B0a83sKNryMEAjkSRymaLjLnXQW1TR3W_5n6W3sJUcCnxxJoL6fqSYByynQQKLXBvOUepFKuwnmjM4HLV5xz01FeU3j9sr8K1lln9w8vf4xV0n0zY76UCv7XcV7UAJQJ7VNhvBKyapOz5TAQFAulNfO3UfxqL4kNK2iW-elcm9heqTLSScqyueFrpb5IgAiY7p5PmTu0yZGtiMaGdz6VT0WpBOLYFVwaMa1rLVrqp92VyzCostzgKARMcsv-UlFFstZrx6mUcq1g_9Xez95gWlAaJCjvfL9JHI--TfzZlhzvWWP5cI13JkJ44Foy3igTJG2noFy9uf6CVrqXrqgs") String auth,
            @RequestHeader(name = "x-source-client-id", defaultValue = "QVBJREEtQkNBWUMtREVW") String xSourceClientId,
            @RequestHeader(name = "x-source-transaction-id", defaultValue = "BYC-12345") String xSourceTransactionId,
            @RequestBody OtpGenerateSingleRequest request
    ) {
        try {

            if (!xSourceTransactionId.startsWith("BYC-")) {
                throw new RuntimeException("Invalid x-source-transaction-id");
            }

            if (xSourceClientId.isBlank()) {
                throw new RuntimeException("Invalid x-source-client-id");
            }
            // Validate authorization token
            if (!Objects.equals(auth, authToken)) {
                log.error("Unauthorized access: Invalid Auth token");
                throw new RuntimeException("No authorization with this token");
            }

            // Call the service and return the response
            ApiBcaResponse.ErrorSchemaResponse response = new ApiBcaResponse.ErrorSchemaResponse();
            response.setError_code("EAI-000");
            response.setError_message(
                    new ApiBcaResponse.ErrorMessageResponse("Success", "Berhasil"));
            return ResponseEntity.ok()
                    .body(new ApiBcaResponse(response, checkCustomerService.generateOtpSingle(product, pan, request)));
        } catch (Exception e) {
            log.error("Error processing request: {}", e.getMessage(), e);
            ApiBcaResponse.ErrorSchemaResponse response = new ApiBcaResponse.ErrorSchemaResponse();
            response.setError_code("MTV");
            response.setError_message(new ApiBcaResponse.ErrorMessageResponse(e.getMessage(), e.getMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiDataResponse(false, e.getMessage(), response));
        }
    }


    @PostMapping("/eai/otp/v3/api/{product}/{pan}/verification")
    public ResponseEntity<?> OtpVerify(
            @Schema(defaultValue = "byc_changepass_sms", description = "product")
            @PathVariable String product,
            @Schema(defaultValue = "0810123456789", description = "pan")
            @PathVariable String pan,
            @RequestHeader(name = "Authorization", defaultValue = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJNZnBVeC15RkFJU2lsQ3pwRFNFYktVbHFtT09zRjZBQV84UnV4MXNIM1VzIn0.eyJleHAiOjE3MjUzNTU5NTksImlhdCI6MTcyNTM1MjM1OSwianRpIjoiZTViNmQxZDMtZDNkOS00OWYwLWE0NGItM2U2YTZiYWM2MWQ1IiwiaXNzIjoiaHR0cHM6Ly9zc28tYXBpZ3ctaW50LmR0aS5jby5pZC9yZWFsbXMvM3NjYWxlLWRldiIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiI5ZGJmNmFjMi1lY2RlLTQ5MTEtODRhOC1hNGZkYThkYzRjNTEiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiI3ZWExMGZmOSIsImFjciI6IjEiLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsib2ZmbGluZV9hY2Nlc3MiLCJkZWZhdWx0LXJvbGVzLTNzY2FsZS1kZXYtbmV3YXJjIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6ImVtYWlsIHByb2ZpbGUiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsImNsaWVudElkIjoiN2VhMTBmZjkiLCJjbGllbnRIb3N0IjoiMTAuNTguODAuNSIsInByZWZlcnJlZF91c2VybmFtZSI6InNlcnZpY2UtYWNjb3VudC03ZWExMGZmOSIsImNsaWVudEFkZHJlc3MiOiIxMC41OC44MC41In0.bo4Ozj5Mq4cSiIwCCeBjFP5_mRe1Vn6tked0MWzlQo8ugnazLT5LnHVaAOZx9CYa2y0f6Zjhcisog5djOIpABlk2nbL22nsHJgpOwShqK1-mLLrgIWsCjZSpBivdSOFGj5W3oZZtWUIaXmeoLX3i9lg342Ge_lAxcUYbexW7qBKpG9pgiv3WtX66lMNnrTM8Ctl69EN4vk5g4UXRHs8hf6FW13r4tcXa9ggp_FmR5klC638A4etUGxz3G9WKIZRDqv2-gFTgTjRP8pRipL6mOdASRZuC1vTn0suGT4ebg9grKUjTApQ5kLNcJv6SjMS_HjJy9Hn_eW8FTmCC6GA5B0a83sKNryMEAjkSRymaLjLnXQW1TR3W_5n6W3sJUcCnxxJoL6fqSYByynQQKLXBvOUepFKuwnmjM4HLV5xz01FeU3j9sr8K1lln9w8vf4xV0n0zY76UCv7XcV7UAJQJ7VNhvBKyapOz5TAQFAulNfO3UfxqL4kNK2iW-elcm9heqTLSScqyueFrpb5IgAiY7p5PmTu0yZGtiMaGdz6VT0WpBOLYFVwaMa1rLVrqp92VyzCostzgKARMcsv-UlFFstZrx6mUcq1g_9Xez95gWlAaJCjvfL9JHI--TfzZlhzvWWP5cI13JkJ44Foy3igTJG2noFy9uf6CVrqXrqgs") String auth,
            @RequestHeader(name = "x-source-client-id", defaultValue = "QVBJREEtQkNBWUMtREVW") String xSourceClientId,
            @RequestHeader(name = "x-source-transaction-id", defaultValue = "BYC-12345") String xSourceTransactionId,
            @RequestBody OtpGenerateVerifyRequest request
    ) {
        try {

            if (!xSourceTransactionId.startsWith("BYC-")) {
                throw new RuntimeException("Invalid x-source-transaction-id");
            }

            if (xSourceClientId.isBlank()) {
                throw new RuntimeException("Invalid x-source-client-id");
            }
            // Validate authorization token
            if (!Objects.equals(auth, authToken)) {
                log.error("Unauthorized access: Invalid Auth token");
                throw new RuntimeException("No authorization with this token");
            }

            // Call the service and return the response
            ApiBcaResponse.ErrorSchemaResponse response = new ApiBcaResponse.ErrorSchemaResponse();
            response.setError_code("EAI-000");
            response.setError_message(
                    new ApiBcaResponse.ErrorMessageResponse("Success", "Berhasil"));
            return ResponseEntity.ok()
                    .body(new ApiBcaResponse(response, checkCustomerService.verifyOtp(product, pan, request)));
        } catch (Exception e) {
            log.error("Error processing request: {}", e.getMessage(), e);
            ApiBcaResponse.ErrorSchemaResponse response = new ApiBcaResponse.ErrorSchemaResponse();
            response.setError_code("MTV");
            response.setError_message(new ApiBcaResponse.ErrorMessageResponse(e.getMessage(), e.getMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiDataResponse(false, e.getMessage(), response));
        }
    }


}
