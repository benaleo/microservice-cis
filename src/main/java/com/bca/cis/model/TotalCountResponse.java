package com.bca.cis.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TotalCountResponse {

    private boolean success;
    private Long totalItems;

}
