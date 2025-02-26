package com.eyedra.user_service_api.controller;


import com.eyedra.user_service_api.dto.request.AuthReqDto;
import com.eyedra.user_service_api.dto.response.AuthResponseDto;

import com.eyedra.user_service_api.services.AuthService;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthReqDto authReq) {
        AuthResponseDto response = authService.login(authReq);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthReqDto authReq) {
        authService.registerUser(authReq);
        return ResponseEntity.ok("User registered!!!");
    }
}
