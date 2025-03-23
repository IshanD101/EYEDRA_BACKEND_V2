package com.eyedra.user_service_api.controller;

import com.eyedra.user_service_api.dto.request.ListenerApplicationReqDto;
import com.eyedra.user_service_api.dto.request.ListenerApplicationReviewReqDto;
import com.eyedra.user_service_api.dto.response.ListenerApplicationResponseDto;
import com.eyedra.user_service_api.services.impl.ListenerApplicationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/listenerApplication")
public class ListenerApplicationController {

    @Autowired
    private ListenerApplicationServiceImpl listenerApplicationService;


    @PostMapping("/apply")
    public ResponseEntity<ListenerApplicationResponseDto> applyForListener(
            @ModelAttribute ListenerApplicationReqDto request) {
        try {
            ListenerApplicationResponseDto response = listenerApplicationService.submitApplication(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/application/{username}")
    public ResponseEntity<ListenerApplicationResponseDto> getApplicationStatus(
            @PathVariable String username) {
        try {
            ListenerApplicationResponseDto response = listenerApplicationService.getApplicationStatus(username);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/applications/pending")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<ListenerApplicationResponseDto>> getPendingApplications() {
        List<ListenerApplicationResponseDto> applications = listenerApplicationService.getPendingApplications();
        return ResponseEntity.ok(applications);
    }

    @PostMapping("/applications/review")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ListenerApplicationResponseDto> reviewApplication(
            @RequestBody ListenerApplicationReviewReqDto request) {
        try {
            ListenerApplicationResponseDto response = listenerApplicationService.reviewApplication(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
