package com.bca.cis.service.impl;

import com.bca.cis.entity.CIS;
import com.bca.cis.model.CISIndividuResponse;
import com.bca.cis.model.GetCISByDebitCardsResponse;
import com.bca.cis.repository.CheckCustomerRepository;
import com.bca.cis.service.CheckCustomerService;
import com.bca.cis.util.Formatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

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
                        "PURWOKERTO"
                )
        );
        cisIndividu.setCustomerNameAndPhone(
                new CISIndividuResponse.CustomerNameAndPhone(
                        data.getName()
                )
        );
        cisIndividu.setCustomerDemographicInformation(
                new CISIndividuResponse.CustomerDemographicInformation(
                        Formatter.formatLocalDate(data.getMemberBirthdate())
                )
        );
        cisIndividu.setCustomerLastUpdate(
                new CISIndividuResponse.CustomerLastUpdate(
                        "2022-01-01",
                        "admin"
                )
        );

        response.setCisIndividu(List.of(cisIndividu));
        return response;
    }
}
