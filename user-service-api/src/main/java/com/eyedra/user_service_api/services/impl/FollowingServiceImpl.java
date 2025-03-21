package com.eyedra.user_service_api.services.impl;

import com.eyedra.user_service_api.dto.response.UserSummaryResponseDto;
import com.eyedra.user_service_api.entity.User;
import com.eyedra.user_service_api.exception.ResourceNotFoundException;
import com.eyedra.user_service_api.repository.UserRepository;
import com.eyedra.user_service_api.services.FollowingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowingServiceImpl implements FollowingService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public void followUser(Long followerId, Long followingId) {
        if (followerId.equals(followingId)) {
            throw new IllegalArgumentException("Users cannot follow themselves");
        }

        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new ResourceNotFoundException("Follower not found with id: " + followerId));

        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new ResourceNotFoundException("User to follow not found with id: " + followingId));

        if (follower.getFollowing().contains(following)) {
            throw new IllegalArgumentException("User is already following this user");
        }

        follower.follow(following);
        userRepository.save(follower);
    }

    @Override
    public void unfollowUser(Long followerId, Long followingId) {

    }

    @Override
    public List<UserSummaryResponseDto> getFollowing(Long userId) {
        return List.of();
    }

    @Override
    public List<UserSummaryResponseDto> getFollowers(Long userId) {
        return List.of();
    }

    @Override
    public long getFollowingCount(Long userId) {
        return 0;
    }

    @Override
    public long getFollowersCount(Long userId) {
        return 0;
    }

    @Override
    public boolean isFollowing(Long followerId, Long followingId) {
        return false;
    }
}
