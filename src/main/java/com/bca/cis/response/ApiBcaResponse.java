package com.bca.cis.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiBcaResponse {
    private ErrorSchemaResponse error_schema;
    private Object output_schema;

    @Data
    public static class ErrorSchemaResponse{
        private String error_code;
        private ErrorMessageResponse error_message;
    }

    @Data
    @AllArgsConstructor
    public static class ErrorMessageResponse {
        private String english;
        private String indonesian;
    }
}
