package com.bca.cis.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CISIndividuResponse {

    private String cisCustomerNumber;
    private String cisCustomerType;
    private CustomerMasterData customerMasterData;
    private CustomerNameAndPhone customerNameAndPhone;
    private CustomerDemographicInformation customerDemographicInformation;
    private CustomerLastUpdate customerLastUpdate;

    @Data
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class CustomerMasterData {
        private String segmentation;
        private String branchNo;
    }

    @Data
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class CustomerNameAndPhone {
        private String fullName;
    }

    @Data
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class CustomerDemographicInformation {
        private String birthDate;
    }

    @Data
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class CustomerLastUpdate {
        private String customerUpdateDate;
        private String customerOfficerUserId;
    }

}
