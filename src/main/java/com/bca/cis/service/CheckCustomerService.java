package com.bca.cis.service;

import com.bca.cis.model.GetCISByDebitCardsResponse;

public interface CheckCustomerService {
    GetCISByDebitCardsResponse findDebitCards(String cardNumber) throws Exception;
}
