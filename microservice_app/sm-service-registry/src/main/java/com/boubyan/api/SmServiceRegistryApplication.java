package com.boubyan.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class SmServiceRegistryApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmServiceRegistryApplication.class, args);
    }

}
