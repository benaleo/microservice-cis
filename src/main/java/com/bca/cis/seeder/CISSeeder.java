package com.bca.cis.seeder;

import com.bca.cis.entity.CIS;
import com.bca.cis.enums.UserType;
import com.bca.cis.repository.CheckCustomerRepository;
import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@AllArgsConstructor
public class CISSeeder {

    private final CheckCustomerRepository checkCustomerRepository;

    @Scheduled(fixedDelay = 50)
    public void run() {
        Faker faker = new Faker();

        String[] memberType = {"SOLITAIRE", "PRIORITY", "NOT_MEMBER"};
        UserType[] userType = {UserType.SL, UserType.PR};

        Long cardNumber = faker.number().randomNumber(16, true);
        Long cinNumber = faker.number().randomNumber(11, true);

        CIS data = new CIS();
        data.setName(faker.name().fullName());
        data.setEmail(faker.internet().emailAddress());
        data.setPhone(faker.phoneNumber().phoneNumber().replace("-", "").replace("x", ""));
        data.setMemberType(userType[faker.number().numberBetween(0, 2)]);
        data.setParentType(userType[faker.number().numberBetween(0, 2)]);
        data.setAccountType(memberType[faker.number().numberBetween(0, 2)]);
        data.setMemberBirthdate(LocalDate.now().minusDays(faker.number().numberBetween(365 * 10, 365 * 15)));
        data.setParentBirthdate(LocalDate.now().minusDays(faker.number().numberBetween(365 * 10, 365 * 45)));
        data.setMemberBankAccount(cardNumber.toString());
        data.setParentBankAccount(cardNumber.toString());
        data.setMemberCin(cinNumber.toString());
        data.setParentCin(cinNumber.toString());

        checkCustomerRepository.save(data);

    }


}
