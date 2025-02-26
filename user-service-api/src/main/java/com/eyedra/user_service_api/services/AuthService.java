package com.eyedra.user_service_api.services;

import com.eyedra.user_service_api.dto.request.AuthReqDto;
import com.eyedra.user_service_api.dto.response.AuthResponseDto;
import com.eyedra.user_service_api.entity.User;
import com.eyedra.user_service_api.jwt.JwtUtils;
import com.eyedra.user_service_api.repository.UserRepository;
import com.eyedra.user_service_api.util.Role;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
@AllArgsConstructor
@Getter
@Setter
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private UserRepository userRepository;

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void upgradeToListener(String username) {
        User user = getUserByUsername(username);
        Set<Role> roles = user.getRoles();
        roles.add(Role.LISTENER);
        userRepository.save(user);
    }

    public void registerUser(AuthReqDto request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setMobileNumber(request.getMobileNumber());
        user.setRoles(Collections.singleton(Role.USER));

        userRepository.save(user);
    }

    public AuthResponseDto login (AuthReqDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtUtils.generateToken(user.getUsername());

        return new AuthResponseDto(token);
    }

}
