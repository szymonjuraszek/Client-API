package com.szymon;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiAplicationApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ApiAplicationApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
