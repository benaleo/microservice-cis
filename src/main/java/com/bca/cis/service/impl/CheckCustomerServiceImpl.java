package com.bca.cis.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.bca.cis.entity.MobileNumber;
import com.bca.cis.entity.Otp;
import com.bca.cis.enums.UserType;
import com.bca.cis.exception.BadRequestException;
import com.bca.cis.model.*;
import com.bca.cis.repository.AppUserRepository;
import com.bca.cis.repository.MobileNumberRepository;
import com.bca.cis.repository.OtpRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.bca.cis.entity.CIS;
import com.bca.cis.repository.CheckCustomerRepository;
import com.bca.cis.service.CheckCustomerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Slf4j
public class CheckCustomerServiceImpl implements CheckCustomerService {

    private final CheckCustomerRepository checkCustomerRepository;
    private final OtpRepository otpRepository;
    private final PasswordEncoder passwordEncoder;
    private final AppUserRepository appUserRepository;
    private final MobileNumberRepository mobileNumberRepository;

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

        Map<String, List<Map<String, Object>>> acct_data = Map.of(
                "acct", List.of(
                        Map.of("acct_no", data.getMemberAccountNumber(), "fact_type", "PF"),
                        Map.of("acct_no", "", "fact_type", ""),
                        Map.of("acct_no", "", "fact_type", ""),
                        Map.of("acct_no", "", "fact_type", ""),
                        Map.of("acct_no", "", "fact_type", "")
                )
        );

        return Map.of(
                "return_code", "00",
                "card_no", data.getMemberBankAccount(),
                "acct_data", acct_data,
                "branch_code", "6903",
                "birth_date", data.getMemberBirthdate().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        );
    }

    @Override
    public Object findInquiryCisRelations(String accountNo) {

        CIS cis = Optional.ofNullable(checkCustomerRepository.findByMemberAccountNumber(accountNo)).orElseThrow(
                () -> new BadRequestException("Account number not found")
        );

        Map<String, Object> customer_master = Map.of(
                "customer_no", cis.getMemberCin(),
                "customer_name", cis.getName(),
                "person_birthdate", cis.getMemberBirthdate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                "membership", cis.getMemberType().name()
        );

        List<Map<String, Object>> relations_cust_to_cust;
        if (cis.getMemberType().equals(UserType.NOT_MEMBER)) {
            relations_cust_to_cust = List.of(
                    Map.of("customer_no_1", cis.getMemberCin(), "customer_no_2", cis.getParentCin(), "relation_code_1_2", "1", "relation_code_2_1", "2"),
                    Map.of("customer_no_1", cis.getMemberCin(), "customer_no_2", cis.getParentCin(), "relation_code_1_2", "103", "relation_code_2_1", "105")
            );
        } else {
            relations_cust_to_cust = new ArrayList<>();
        }

        return Map.<String, Object>of(
                "get_by_account_no", Map.of(
                        "list_data", List.of(Map.of(
                                "customer_master", customer_master,
                                "relations_cust_to_cust", relations_cust_to_cust
                        ))
                )
        );
    }

    @Override
    public List<Map<String, Object>> findInquiryMobileNumber(String customerNumber) {
        CIS cis = Optional.ofNullable(checkCustomerRepository.findByMemberCin(customerNumber)).orElseThrow(
                () -> new BadRequestException("Account number not found")
        );
        log.info("cis name : {}", cis.getName());

        List<MobileNumber> mobileNumbers = mobileNumberRepository.findByCis(cis);
        log.info("size of mobile number : {}", mobileNumbers != null ? mobileNumbers.size() : 0);

        List<Map<String, Object>> mapMobileNumbers = new ArrayList<>();
        if (mobileNumbers != null && !mobileNumbers.isEmpty()) {
            for (MobileNumber mobileNumber : mobileNumbers) {
                mapMobileNumbers.add(Map.of(
                        "phone", Optional.ofNullable(mobileNumber.getPhone()).orElse(""),
                        "code", Optional.ofNullable(mobileNumber.getCode()).orElse("62"),
                        "status", Optional.ofNullable(mobileNumber.getStatus()).orElse("")
                ));
            }
        }

        return mapMobileNumbers;
    }

    @Override
    public Object generateOtpSingle(String product, String pan, OtpGenerateSingleRequest request) {
        Otp otp = new Otp();
//        String generate8RandomDigits = RandomStringUtils.randomNumeric(6).toUpperCase();
        otp.setOtp("123456");
        otp.setUser(appUserRepository.findById(2L).orElse(null));
        otp.setExpiredAt(LocalDateTime.now().plusMinutes(5));
        otp.setIsValid(true);
        otp.setPhone(pan);
        otpRepository.save(otp);

        return Map.of(
                "otp_code", "123456",
                "message_reference_no", "DKNOTP08102100000011473",
                "pan", pan,
                "product", product,
                "reference_code", "00038621",
                "expired_date", LocalDateTime.now().plusMinutes(5).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                "status", "SUCCESS",
                "counter", "1"
        );
    }

    @Override
    public Object verifyOtp(String product, String pan, OtpGenerateVerifyRequest request) {
        List<Otp> otp = otpRepository.findAllByOtpAndPhoneOrderByIdDesc(request.getOtp(), pan);

        if (otp.isEmpty()) {
            throw new BadRequestException("OTP not found");
        }
        Otp lastOtp = otp.getFirst();
        lastOtp.setIsValid(false);
        otpRepository.save(lastOtp);

        return Map.of(
                "otp_code", lastOtp.getOtp(),
                "phone_number", pan,
                "pan", pan,
                "product", product,
                "email_address", "",
                "reference_code", "00038621",
                "expired_date", LocalDateTime.now().plusMinutes(5).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                "status", "SUCCESS",
                "counter", "1"
        );
    }
}
