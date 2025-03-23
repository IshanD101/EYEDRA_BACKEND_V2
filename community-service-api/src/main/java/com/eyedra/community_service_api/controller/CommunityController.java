package com.eyedra.community_service_api.controller;

import com.eyedra.community_service_api.dto.request.CommunityReqDto;
import com.eyedra.community_service_api.dto.response.CommunityResponseDto;
import com.eyedra.community_service_api.dto.response.TitleResponse;
import com.eyedra.community_service_api.service.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/communities")
public class CommunityController {

    @Autowired
    private CommunityService communityService;

    @PostMapping
    public Mono<ResponseEntity<CommunityResponseDto>> createGroup(
            @RequestHeader("Authorization") String token,
            @RequestParam Long creatorId,
            @RequestBody CommunityReqDto request) {
        return communityService.createGroup(token.replace("Bearer ", ""), creatorId, request)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }

    @GetMapping("/{communityId}")
    public Mono<ResponseEntity<CommunityResponseDto>> getGroupById(
            @PathVariable Long communityId) {
        return communityService.getGroupById(communityId)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{communityId}")
    public Mono<ResponseEntity<CommunityResponseDto>> updateGroup(
            @RequestHeader("Authorization") String token,
            @RequestParam Long userId,
            @PathVariable Long communityId,
            @RequestBody CommunityReqDto request) {
        return communityService.updateGroup(token.replace("Bearer ", ""), userId, communityId, request)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{communityId}")
    public Mono<ResponseEntity<Void>> deleteGroup(
            @RequestHeader("Authorization") String token,
            @RequestParam Long userId,
            @PathVariable Long communityId) {
        return communityService.deleteGroup(token.replace("Bearer ", ""), userId, communityId)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()));
    }

    @PostMapping("/{communityId}/members/{memberId}")
    public Mono<ResponseEntity<Void>> addMember(
            @RequestHeader("Authorization") String token,
            @RequestParam Long requesterId,
            @PathVariable Long communityId,
            @PathVariable Long memberId) {
        return communityService.addMembers(token.replace("Bearer ", ""), requesterId, communityId, memberId)
                .then(Mono.just(ResponseEntity.status(HttpStatus.CREATED).<Void>build()));
    }

    @DeleteMapping("/{communityId}/members/{memberId}")
    public Mono<ResponseEntity<Void>> leaveGroup(
            @PathVariable Long communityId,
            @PathVariable Long memberId) {
        return communityService.leaveGroup(memberId, communityId)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()));
    }

    @GetMapping("/search")
    public Mono<ResponseEntity<List<TitleResponse>>> searchByTitle(
            @RequestParam(required = false) String title) {
        return communityService.searchByGroupTitle(title)
                .map(ResponseEntity::ok);
    }
}