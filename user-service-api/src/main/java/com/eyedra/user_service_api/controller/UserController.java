package com.eyedra.user_service_api.controller;

import com.eyedra.user_service_api.entity.User;
import com.eyedra.user_service_api.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController("/api/v1/user")
@CrossOrigin
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{username}")
    public User getUser(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @PostMapping("/convert/{username}")
    public String convertToListener(@PathVariable String username) {
        userService.upgradeToListener(username);
        return "User " + username + " upgraded to a listener";
    }
}
