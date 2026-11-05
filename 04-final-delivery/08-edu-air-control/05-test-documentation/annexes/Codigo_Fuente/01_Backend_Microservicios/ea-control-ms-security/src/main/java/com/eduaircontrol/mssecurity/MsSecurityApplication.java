package com.eduaircontrol.mssecurity;

import java.time.Clock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MsSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsSecurityApplication.class, args);
    }

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }
}
