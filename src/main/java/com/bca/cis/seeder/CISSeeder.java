package com.bca.cis.seeder;

import com.bca.cis.entity.CIS;
import com.bca.cis.entity.MobileNumber;
import com.bca.cis.enums.UserType;
import com.bca.cis.repository.CheckCustomerRepository;
import com.bca.cis.repository.MobileNumberRepository;
import com.bca.cis.util.PhoneNumberGenerator;
import com.bca.cis.util.SecureIdGenerator;
import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class CISSeeder {

    private final CheckCustomerRepository checkCustomerRepository;
    private final MobileNumberRepository mobileNumberRepository;

//    @Scheduled(fixedDelay = 50)
    public void run() {
        Faker faker = new Faker();

        String[] memberType = {"SOLITAIRE", "PRIORITY", "NOT_MEMBER"};
        UserType[] userType = {UserType.SL, UserType.PR};
        String[] status = {"1", "2"};

        long cardNumber = faker.number().randomNumber(16, true);
        long cinNumber = faker.number().randomNumber(11, true);
        long accountNumber = faker.number().randomNumber(10, true);

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
        for (int i = 0; i < 4; i++) {
            MobileNumber newMobileNumber = new MobileNumber();
            newMobileNumber.setSecureId(SecureIdGenerator.generateSecureUUID());
            newMobileNumber.setPhone(PhoneNumberGenerator.generateRandomPhoneNumber());
            newMobileNumber.setCis(saved);
            newMobileNumber.setStatus(status[faker.number().numberBetween(0, 2)]);
            mobileNumbers.add(newMobileNumber);
        }
        mobileNumberRepository.saveAll(mobileNumbers);


    }


}
