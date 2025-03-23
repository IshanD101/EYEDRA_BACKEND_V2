package com.eyedra.post_service_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableDiscoveryClient
@EnableReactiveMongoRepositories(basePackages = "com.eyedra.post_service_api.repository")
public class PostServiceApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(PostServiceApiApplication.class, args);
    }
}
