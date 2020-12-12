package com.onyshchenko.psabot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
public class PsaBotApplication {

    public static void main(String[] args) {

        ApiContextInitializer.init();

        SpringApplication.run(PsaBotApplication.class, args);
    }

}
