package com.bca.cis.service;

import com.bca.cis.model.GetCISByDebitCardsResponse;

public interface CheckCustomerService {
    GetCISByDebitCardsResponse findDebitCards(String cardNumber) throws Exception;

    Object findAccountNumberChecks(String applicationCode, String accountNumber);

    Object findRelationChecks(String cisCustomerNumber);

    Object findAcctFactByNumber(String cardNumber);
}
