package com.eyedra.user_service_api.controller;

import com.eyedra.user_service_api.dto.request.ProfileUpdateReqDto;
import com.eyedra.user_service_api.entity.User;
import com.eyedra.user_service_api.services.AuthService;
import com.eyedra.user_service_api.services.impl.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin("*")
public class UserController {

    private final AuthService authService;
    private final UserServiceImpl userService;

    public UserController(AuthService authService, UserServiceImpl userService) {
        this.authService = authService;
        this.userService = userService;
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

    @PutMapping("/update/{username}")
    public ResponseEntity<String> updateUser(Authentication authentication,
                                             @RequestBody ProfileUpdateReqDto updateReqDto) {
        String username = authentication.getName();
        userService.updateProfile(username, updateReqDto);

        return ResponseEntity.ok("User " + username + " updated");
    }
}
