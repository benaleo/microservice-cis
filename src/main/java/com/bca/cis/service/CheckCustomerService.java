package com.bca.cis.service;

import com.bca.cis.model.GetCISByDebitCardsResponse;
import com.bca.cis.model.OtpGenerateSingleRequest;

public interface CheckCustomerService {
    GetCISByDebitCardsResponse findDebitCards(String cardNumber) throws Exception;

    Object findAccountNumberChecks(String applicationCode, String accountNumber);

    Object findRelationChecks(String cisCustomerNumber);

    Object findAcctFactByNumber(String cardNumber);

    Object findInquiryCisRelations(String accountNo);

    Object findInquiryMobileNumber(String customerNumber, String countryCd, String phone);

    Object generateOtpSingle(String product, String pan, OtpGenerateSingleRequest request);
}
