package com.eduaircontrol.mssensor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MsSensorManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsSensorManagementApplication.class, args);
    }
}
