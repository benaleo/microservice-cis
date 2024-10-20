package com.bca.cis.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiDataResponse {

    private Boolean success;
    private String message;
    private Object data;
}
