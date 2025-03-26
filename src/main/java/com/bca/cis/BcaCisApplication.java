package com.bca.cis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
//@Import(HcpImpl.class)
public class BcaCisApplication {

    public static void main(String[] args) {
        SpringApplication.run(BcaCisApplication.class, args);
    }

}
