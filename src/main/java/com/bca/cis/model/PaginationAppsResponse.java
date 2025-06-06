package com.bca.cis.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class PaginationAppsResponse<T> implements Serializable {

    private boolean success;
    private String message;
    private Object data;

}
