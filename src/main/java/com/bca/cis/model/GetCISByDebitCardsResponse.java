package com.bca.cis.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class GetCISByDebitCardsResponse {

    private List<CISIndividuResponse> cisIndividu = new ArrayList<>();

    private List<CISOrganizationResponse> cisOrganization = new ArrayList<>();

}
