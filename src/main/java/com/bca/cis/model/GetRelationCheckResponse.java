package com.bca.cis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetRelationCheckResponse {

    private List<CheckParentResponse> list_customer = new ArrayList<>();
    private String total_data = "0";

    @Data
    @AllArgsConstructor
    public static class CheckParentResponse{
        private String customer_number;
        private String customer_type;
        private String customer_name;
        private String application_code;
        private String relation_type;
        private String customer_relation_date;
        private String relation_code_1;
        private String relation_code_2;
        private String relation_description_1;
        private String relation_description_2;
    }

}
