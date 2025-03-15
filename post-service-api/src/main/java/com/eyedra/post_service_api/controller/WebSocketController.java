package com.eyedra.post_service_api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebSocketController {

    @GetMapping("/websocket")
    public String webSocketPage() {
        return "websocket"; // Returns a Thymeleaf view (websocket.html)
    }
}
