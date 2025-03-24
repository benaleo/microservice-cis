package com.bca.cis.model;

import lombok.Data;

import java.util.List;

@Data
public class OtpGenerateSingleRequest {
    private String expired_param;
    private String additional_info;
    private String email_address;
    private String phone_number;
    private String message_content;
    private List<String> message_parameters;
    private List<keyValue> email_parameters;


    @Data
    public static class keyValue {
        private String key;
        private String value;
    }
}
