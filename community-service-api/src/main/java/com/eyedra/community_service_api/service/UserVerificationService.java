package com.eyedra.community_service_api.service;

import reactor.core.publisher.Mono;

public interface UserVerificationService {

    Mono<Boolean> isListener(String token, Long userId);
    Mono<Boolean> isAdmin(String token, Long userId);
    Mono<Boolean> isUser(String token, Long userId);
}
