package com.eyedra.community_service_api.service.impl;

import com.eyedra.community_service_api.configs.WebClientConfig;
import com.eyedra.community_service_api.service.UserVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class UserVerificationServiceImpl implements UserVerificationService {

    @Autowired
    private WebClient userServiceWebClient;

    @Override
    public Mono<Boolean> isListener(String token, Long userId) {
        return userServiceWebClient.get()
                .uri("/{userId}/role/listener", userId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorReturn(false);
    }

    @Override
    public Mono<Boolean> isAdmin(String token, Long userId) {
        return userServiceWebClient.get()
                .uri("/{userId}/role/admin", userId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorReturn(false);
    }

    @Override
    public Mono<Boolean> isUser(String token, Long userId) {
        return userServiceWebClient.get()
                .uri("/{userId}/role/user", userId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorReturn(false);

    }
}
