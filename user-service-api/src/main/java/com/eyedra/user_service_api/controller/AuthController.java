package com.eyedra.user_service_api.controller;


import com.eyedra.user_service_api.dto.request.AuthReqDto;
import com.eyedra.user_service_api.dto.response.AuthResponseDto;
import com.eyedra.user_service_api.entity.User;
import com.eyedra.user_service_api.jwt.JwtUtils;
import com.eyedra.user_service_api.repository.UserRepository;
import com.eyedra.user_service_api.util.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RequiredArgsConstructor
@RestController("/api/v1/auth")
@CrossOrigin
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtils jwtUtil;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public AuthResponseDto login(@RequestBody AuthReqDto request){
        authManager.authenticate(new UsernamePasswordAuthenticationToken
                (request.getUserName(), request.getPassword()));
        String token = jwtUtil.generateToken(request.getUserName());
        return new AuthResponseDto(token);
    }


    @PostMapping("/signup")
    public AuthResponseDto signUp(@RequestBody AuthReqDto request){
        if (userRepo.findByUserName(request.getUserName()).isPresent()){
            throw new RuntimeException("Username exists!");
        }

        User user = User.builder()
                .userName(request.getUserName())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Collections.singleton(Role.USER))
                .build();

        userRepo.save(user);

        String token = jwtUtil.generateToken(user.getUsername());
        return new AuthResponseDto(token);
    }
}
