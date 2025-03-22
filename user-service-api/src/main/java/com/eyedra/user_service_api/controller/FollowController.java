package com.eyedra.user_service_api.controller;

import com.eyedra.user_service_api.dto.response.UserSummaryResponseDto;
import com.eyedra.user_service_api.entity.User;
import com.eyedra.user_service_api.repository.UserRepository;
import com.eyedra.user_service_api.services.impl.FollowingServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class FollowController {
    private final UserRepository userRepository;
    private final FollowingServiceImpl followService;

    @PostMapping("/{followingId}/follow")
    @PreAuthorize("hasAnyRole('USER', 'LISTENER')")
    public ResponseEntity<?> followUser(@PathVariable Long followingId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        Long currentUserId = currentUser.getUserId();

        followService.followUser(currentUserId, followingId);

        return ResponseEntity.ok().build();

    }

    @DeleteMapping("/{followingId}/unfollow")
    @PreAuthorize("hasAnyRole('USER', 'LISTENER')")
    public ResponseEntity<?> unfollowUser(@PathVariable Long followingId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        Long currentUserId = currentUser.getUserId();

        followService.unfollowUser(currentUserId, followingId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<List<UserSummaryResponseDto>> getFollowing(@PathVariable Long userId) {
        List<UserSummaryResponseDto> following = followService.getFollowing(userId);
        return ResponseEntity.ok(following);
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<UserSummaryResponseDto>> getFollowers(@PathVariable Long userId) {
        List<UserSummaryResponseDto> followers = followService.getFollowers(userId);
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/{userId}/following/count")
    public ResponseEntity<Map<String, Long>> getFollowingCount(@PathVariable Long userId) {
        long count = followService.getFollowingCount(userId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    @GetMapping("/{userId}/followers/count")
    public ResponseEntity<Map<String, Long>> getFollowersCount(@PathVariable Long userId) {
        long count = followService.getFollowersCount(userId);
        return ResponseEntity.ok(Map.of("count", count));
    }
}
