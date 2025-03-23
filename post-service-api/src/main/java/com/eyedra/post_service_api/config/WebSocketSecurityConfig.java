package com.eyedra.post_service_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSocketSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .requestMatchers("/ws/**").permitAll()  // Allow WebSocket connections without authentication
                .anyRequest().authenticated()          // Protect other requests
                .and()
                .csrf(csrf -> csrf.ignoringRequestMatchers("/ws/**"));  // Disable CSRF only for WebSocket

        return http.build();
    }
}
