package com.eyedra.community_service_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CommunityServiceApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommunityServiceApiApplication.class, args);
	}

}
