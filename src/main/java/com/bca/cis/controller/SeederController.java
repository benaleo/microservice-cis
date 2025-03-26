package com.bca.cis.controller;

import com.bca.cis.entity.CIS;
import com.bca.cis.entity.MobileNumber;
import com.bca.cis.enums.UserType;
import com.bca.cis.repository.CheckCustomerRepository;
import com.bca.cis.repository.MobileNumberRepository;
import com.bca.cis.util.PhoneNumberGenerator;
import com.bca.cis.util.SecureIdGenerator;
import com.github.javafaker.Faker;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(SeederController.urlRoute)
@Tag(name = "Seeder API")
public class SeederController {

    static final String urlRoute = "/api/v1/seeder";

    private final MobileNumberRepository mobileNumberRepository;
    private final CheckCustomerRepository checkCustomerRepository;

    @GetMapping
    public ResponseEntity<String> seeder(@RequestParam Integer size) {
        Faker faker = new Faker();

        String[] memberType = {"SOLITAIRE", "PRIORITY", "NOT_MEMBER"};
        UserType[] userType = {UserType.SL, UserType.PR};
        String[] status = {"1", "2"};

        long cardNumber = faker.number().randomNumber(16, true);
        long cinNumber = faker.number().randomNumber(11, true);
        long accountNumber = faker.number().randomNumber(10, true);

        for (int i = 0; i < size; i++) {
            CIS data = new CIS();
            data.setName(faker.name().fullName());
            data.setEmail(faker.internet().emailAddress());
            data.setPhone(PhoneNumberGenerator.generateRandomPhoneNumber());
            data.setMemberType(userType[faker.number().numberBetween(0, 2)]);
            data.setParentType(userType[faker.number().numberBetween(0, 2)]);
            data.setAccountType(memberType[faker.number().numberBetween(0, 2)]);
            data.setMemberBirthdate(LocalDate.now().minusDays(faker.number().numberBetween(365 * 10, 365 * 15)));
            data.setParentBirthdate(LocalDate.now().minusDays(faker.number().numberBetween(365 * 10, 365 * 45)));
            data.setMemberBankAccount(String.valueOf(cardNumber));
            data.setParentBankAccount(String.valueOf(cardNumber));
            data.setMemberCin(String.valueOf(cinNumber));
            data.setParentCin(String.valueOf(cinNumber));
            data.setMemberAccountNumber(String.valueOf(accountNumber));
            data.setParentAccountNumber(String.valueOf(accountNumber));

            CIS saved = checkCustomerRepository.save(data);

            MobileNumber mobileNumber = new MobileNumber();
            mobileNumber.setSecureId(SecureIdGenerator.generateSecureUUID());
            mobileNumber.setPhone(saved.getPhone());
            mobileNumber.setStatus("1");
            mobileNumber.setCis(saved);

            List<MobileNumber> mobileNumbers = new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                MobileNumber newMobileNumber = new MobileNumber();
                newMobileNumber.setSecureId(SecureIdGenerator.generateSecureUUID());
                newMobileNumber.setPhone(PhoneNumberGenerator.generateRandomPhoneNumber());
                newMobileNumber.setCis(saved);
                newMobileNumber.setStatus(status[faker.number().numberBetween(0, 2)]);
                mobileNumbers.add(newMobileNumber);
            }
            mobileNumberRepository.saveAll(mobileNumbers);
        }
        return ResponseEntity.ok().body("Success");
    }

}
