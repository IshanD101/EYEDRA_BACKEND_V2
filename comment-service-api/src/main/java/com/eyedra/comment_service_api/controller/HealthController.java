package com.eyedra.comment_service_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/comments/health")
public class HealthController {

    @GetMapping
    public String healthCheck() {
        return "Socket service is running on port 5001";
    }
}
