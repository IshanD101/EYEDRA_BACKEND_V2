package com.eyedra.user_service_api.controller;

import com.eyedra.user_service_api.dto.request.ListenerProfileDto;
import com.eyedra.user_service_api.dto.request.ListenerProfileUpdateReqDto;
import com.eyedra.user_service_api.dto.request.ListenerStatsDto;
import com.eyedra.user_service_api.services.ListenerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/listener")
@CrossOrigin("*")
public class ListenerController {

    private final ListenerService listenerService;

    @GetMapping("/lDashboard")
    public String listenerDashboard() {
        return "Welcome to Listener dashboard!!!!!!";
    }

    @GetMapping("/profile")
    public ResponseEntity<ListenerProfileDto> getListenerProfile(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(listenerService.getListenerProfile(username));
    }

    @PutMapping("/profile")
    public ResponseEntity<String> updateListenerProfile(Authentication authentication,
                                                        @RequestBody ListenerProfileUpdateReqDto updateDto) {
        String username = authentication.getName();
        listenerService.updateListenerProfile(username, updateDto);
        return ResponseEntity.ok("Listener profile updated successfully");
    }

    @GetMapping("/stats")
    public ResponseEntity<ListenerStatsDto> getListenerStats(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(listenerService.getListenerStats(username));
    }
}
