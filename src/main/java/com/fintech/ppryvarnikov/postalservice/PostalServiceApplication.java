package com.fintech.ppryvarnikov.postalservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PostalServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PostalServiceApplication.class, args);
    }
}
