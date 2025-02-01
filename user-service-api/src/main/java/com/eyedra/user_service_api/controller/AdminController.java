package com.eyedra.user_service_api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController("/api/v1/admin")
public class AdminController {

    @GetMapping("/aDashboard")
    public String adminDashboard() {
        return "Welcome to the Admin Dashboard!";
    }
}
