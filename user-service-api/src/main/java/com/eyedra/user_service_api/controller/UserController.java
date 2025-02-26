package com.eyedra.user_service_api.controller;

import com.eyedra.user_service_api.entity.User;
import com.eyedra.user_service_api.services.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController("/api/v1/user")
@CrossOrigin
public class UserController {

    private final AuthService authService;

    public UserController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/{username}")
    public User getUser(@PathVariable String username) {
        return authService.getUserByUsername(username);
    }

    @PostMapping("/convert/{username}")
    public String convertToListener(@PathVariable String username) {
        authService.upgradeToListener(username);
        return "User " + username + " upgraded to a listener";
    }
}
