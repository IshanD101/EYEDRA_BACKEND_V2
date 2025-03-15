package com.eyedra.community_service_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })

public class CommunityServiceApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommunityServiceApiApplication.class, args);
	}

}
