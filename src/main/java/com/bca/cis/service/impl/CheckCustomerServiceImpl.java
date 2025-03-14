package com.bca.cis.service.impl;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.bca.cis.entity.CIS;
import com.bca.cis.model.CISIndividuResponse;
import com.bca.cis.model.GetCISByDebitCardsResponse;
import com.bca.cis.model.GetRelationCheckResponse;
import com.bca.cis.repository.CheckCustomerRepository;
import com.bca.cis.service.CheckCustomerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CheckCustomerServiceImpl implements CheckCustomerService {

    private final CheckCustomerRepository checkCustomerRepository;

    @Override
    public GetCISByDebitCardsResponse findDebitCards(String cardNumber) {
        if (!checkCustomerRepository.existsByMemberBankAccount(cardNumber)) {
            throw new HttpClientErrorException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "Card number not found");
        }

        CIS data = checkCustomerRepository.findByMemberBankAccount(cardNumber);

        GetCISByDebitCardsResponse response = new GetCISByDebitCardsResponse();
        CISIndividuResponse cisIndividu = new CISIndividuResponse();
        cisIndividu.setCisCustomerNumber(data.getMemberBankAccount());
        cisIndividu.setCisCustomerType(data.getMemberType().name());
        cisIndividu.setCustomerMasterData(
                new CISIndividuResponse.CustomerMasterData(
                        data.getAccountType(),
                        "0120"
                )
        );
        cisIndividu.setCustomerNameAndPhone(
                new CISIndividuResponse.CustomerNameAndPhone(
                        data.getName()
                )
        );
        cisIndividu.setCustomerDemographicInformation(
                new CISIndividuResponse.CustomerDemographicInformation(
                        data.getMemberBirthdate().format(DateTimeFormatter.ofPattern("ddMMyyyy"))
                )
        );
        cisIndividu.setCustomerLastUpdate(
                new CISIndividuResponse.CustomerLastUpdate(
                        "01012022",
                        "admin"
                )
        );

        response.setCisIndividu(List.of(cisIndividu));
        return response;
    }

    @Override
    public Object findAccountNumberChecks(String applicationCode, String accountNumber) {
        return null;
    }

    @Override
    public Object findRelationChecks(String cisCustomerNumber) {
        CIS data = checkCustomerRepository.findByParentBankAccount(cisCustomerNumber);


        return new GetRelationCheckResponse(
                List.of(new GetRelationCheckResponse.CheckParentResponse(
                        data.getParentBankAccount(),
                        data.getParentType().name(),
                        "",
                        "", "", "", "", "", "", ""
                )),
                "1"
        );
    }

    @Override
    public Object findAcctFactByNumber(String cardNumber) {
        if (!checkCustomerRepository.existsByMemberBankAccount(cardNumber)) {
            throw new HttpClientErrorException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "Card number not found");
        }

        CIS data = checkCustomerRepository.findByMemberBankAccount(cardNumber);

        Map<String, List<Map<String, Object>>> acct_data = new HashMap<String, List<Map<String, Object>>>();
        List<Map<String, Object>> acct = List.of(
                Map.of("acct_no", data.getMemberBankAccount(), "fact_type", "PF"),
                Map.of("acct_no", "", "fact_type", ""),
                Map.of("acct_no", "", "fact_type", ""),
                Map.of("acct_no", "", "fact_type", ""),
                Map.of("acct_no", "", "fact_type", "")
        );
        acct_data.put("acct", acct);

        Map<String, Object> output = new HashMap<>();
        output.put("return_code", "00");
        output.put("card_no", data.getMemberBankAccount());
        output.put("acct_data", acct_data);

        return output;
    }

    @Override
    public Object findInquiryCisRelations(String accountNo) {

        Map<String, Object> customer_master = Map.of(
                "customer_no", accountNo,
                "customer_name", "Diantdra",
                "person_birthdate", "1990-01-01",
                "membership", "SL"
        );

        List<Map<String, Object>> relations_cust_to_cust = List.of(
                Map.of("customer_no_1", "3", "customer_no_2", "4", "relation_code_1_2", "1", "relation_code_2_1", "2"),
                Map.of("customer_no_1", "3", "customer_no_2", "4", "relation_code_1_2", "103", "relation_code_2_1", "105")
        );

        Map<String, Object> list_data = Map.of(
                "customer_master", customer_master,
                "relations_cust_to_cust", relations_cust_to_cust
        );

        Map<String, Object> getByAccountNo = Map.of(
                "list_data", List.of(list_data)
        );

        Map<String, Object> output = Map.of(
                "get_by_account_no", getByAccountNo
        );

        return output;
    }

@Override
public Object findInquiryMobileNumber(String customerNumber, String countryCd, String phone) {
  
        return null;
}
}
