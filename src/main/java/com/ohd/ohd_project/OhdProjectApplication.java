package com.ohd.ohd_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories(basePackages = "com.ohd.ohd_project.repository")
@EntityScan(basePackages = "com.ohd.ohd_project.model")
public class OhdProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(OhdProjectApplication.class, args);
    }
}