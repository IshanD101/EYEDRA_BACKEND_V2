package com.eyedra.user_service_api.controller;


import com.eyedra.user_service_api.dto.request.LoginReqDto;
import com.eyedra.user_service_api.dto.request.RegisterReqDto;
import com.eyedra.user_service_api.dto.response.AuthResponseDto;

import com.eyedra.user_service_api.services.AuthService;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/auth",
        produces = "application/json",
        consumes = "application/json")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginReqDto loginReqDto) {
        AuthResponseDto response = authService.login(loginReqDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterReqDto authReq) {
        authService.registerUser(authReq);
        return "User Registered Successfully....!!!";
    }
}
