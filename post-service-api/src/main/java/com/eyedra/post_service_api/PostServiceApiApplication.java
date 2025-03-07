package com.eyedra.post_service_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PostServiceApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PostServiceApiApplication.class, args);
	}

}
