package com.eyedra.reaction_service_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ReactionServiceApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactionServiceApiApplication.class, args);
	}

}
