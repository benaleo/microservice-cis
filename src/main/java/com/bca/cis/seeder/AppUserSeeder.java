package com.bca.cis.seeder;

import com.bca.cis.entity.AppUser;
import com.bca.cis.repository.AppUserRepository;
import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class AppUserSeeder {

    private final AppUserRepository userRepository;

//    @Scheduled(fixedDelay = 50)
    public void run() {
        Faker faker = new Faker();

        AppUser newUser = new AppUser();
        newUser.setId(null);
        newUser.setName(faker.name().fullName());
        newUser.setEmail(faker.internet().emailAddress());
        newUser.setUsername(faker.name().username());
        newUser.setPassword(faker.internet().password());

        userRepository.save(newUser);

    }
}
