package com.eyedra.community_service_api.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${user-service-api.url}")
    private String userServiceUrl;

    @Bean
    public WebClient userServiceWebClient() {
        return WebClient.builder()
                .baseUrl(userServiceUrl + "/api/v1/user")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}