package com.eyedra.community_service_api.controller;

import com.eyedra.community_service_api.dto.request.MessageReqDto;
import com.eyedra.community_service_api.dto.response.MessageResponseDto;
import com.eyedra.community_service_api.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/communities/{communityId}")
    public Mono<ResponseEntity<MessageResponseDto>> sendMessage(
            @PathVariable Long communityId,
            @RequestParam Long senderId,
            @RequestBody MessageReqDto request) {
        return messageService.sendMessage(communityId, senderId, request)
                .map(dto -> new ResponseEntity<>(dto, HttpStatus.CREATED));
    }

    @GetMapping("/communities/{communityId}")
    public Mono<ResponseEntity<List<MessageResponseDto>>> getGroupMessages(
            @PathVariable Long communityId,
            @RequestParam Long userId) {
        return messageService.getGroupMessages(communityId, userId)
                .map(list -> new ResponseEntity<>(list, HttpStatus.OK));
    }

    @PutMapping("/communities/{communityId}/read")
    public Mono<ResponseEntity<Void>> markMessagesAsRead(
            @PathVariable Long communityId,
            @RequestParam Long userId) {
        return messageService.markMessagesAsRead(communityId, userId)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)));
    }

    @GetMapping("/communities/{communityId}/unread")
    public Mono<ResponseEntity<Long>> getUnreadMessagesCount(
            @PathVariable Long communityId,
            @RequestParam Long userId) {
        return messageService.getUnreadMessagesCount(communityId, userId)
                .map(count -> new ResponseEntity<>(count, HttpStatus.OK));
    }

    @GetMapping("/unread")
    public Mono<ResponseEntity<Long>> getTotalUnreadMessagesCount(
            @RequestParam Long userId) {
        return messageService.getTotalUnreadMessagesCount(userId)
                .map(count -> new ResponseEntity<>(count, HttpStatus.OK));
    }

    @PostMapping("/latest")
    public Mono<ResponseEntity<List<MessageResponseDto>>> getLatestMessages(
            @RequestParam Long userId,
            @RequestBody List<Long> communityIds) {
        return messageService.getLatestMessages(communityIds, userId)
                .map(list -> new ResponseEntity<>(list, HttpStatus.OK));
    }
}