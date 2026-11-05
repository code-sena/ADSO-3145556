package com.eduaircontrol.msclassroom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MsClassroomManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsClassroomManagementApplication.class, args);
    }
}
