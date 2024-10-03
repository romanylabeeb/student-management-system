package com.boubyan.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SmGatewayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmGatewayServiceApplication.class, args);
	}

}
