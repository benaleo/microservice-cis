package com.bca.cis.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bca.cis.entity.AppUser;
import com.bca.cis.entity.Otp;
import com.bca.cis.model.OtpGenerateSingleRequest;
import com.bca.cis.repository.AppUserRepository;
import com.bca.cis.repository.OtpRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final OtpRepository otpRepository;
    private final PasswordEncoder passwordEncoder;
    private final AppUserRepository appUserRepository;

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
public Map<String, Object> findInquiryMobileNumber(String customerNumber, String countryCd, String phone) {
        Map<String, Object> output = Map.of(
                "cust_no", "00",
                "status", "1",
                "country_cd", countryCd,
                "phone", phone,
                "operator", "VIANNY PANGESA"
        );
  
        return output;
}

    @Override
    public Object generateOtpSingle(String product, String pan, OtpGenerateSingleRequest request) {
        Otp otp = new Otp();
        String generate8RandomDigits = RandomStringUtils.randomAlphanumeric(8).toUpperCase();
        otp.setOtp(generate8RandomDigits);
        otp.setUser(appUserRepository.findById(2L).orElse(null));
        otp.setExpiredAt(LocalDateTime.now().plusMinutes(5));
        otpRepository.save(otp);

        return Map.of(
                "output_schema", Map.of(
                        "otp_code", generate8RandomDigits,
                        "message_reference_no", "DKNOTP08102100000011473",
                        "pan", pan,
                        "product", product,
                        "reference_code", "00038621",
                        "expired_date", LocalDateTime.now().plusMinutes(5).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                        "status", "SUCCESS",
                        "counter", "1"
                )
        );
    }
}
