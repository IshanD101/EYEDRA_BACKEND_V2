package com.eyedra.user_service_api.services.impl;

import com.eyedra.user_service_api.dto.response.UserSummaryResponseDto;
import com.eyedra.user_service_api.entity.User;
import com.eyedra.user_service_api.exception.ResourceNotFoundException;
import com.eyedra.user_service_api.repository.UserRepository;
import com.eyedra.user_service_api.services.FollowingService;
import com.eyedra.user_service_api.util.UserSummaryMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowingServiceImpl implements FollowingService {

    private final UserRepository userRepository;
    private final UserSummaryMapper userSummaryMapper;

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
    @Transactional
    public void unfollowUser(Long followerId, Long followingId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new ResourceNotFoundException("Follower not found with id: " + followerId));

        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new ResourceNotFoundException("User to unfollow not found with id: " + followingId));

        if (!follower.getFollowing().contains(following)) {
            throw new IllegalArgumentException("User is not following this user");
        }

        follower.unfollow(following);
        userRepository.save(follower);
    }

    @Override
    public List<UserSummaryResponseDto> getFollowing(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return user.getFollowing().stream()
                .map(userSummaryMapper::mapToUserSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserSummaryResponseDto> getFollowers(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return user.getFollowers().stream()
                .map(userSummaryMapper::mapToUserSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    public long getFollowingCount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return user.getFollowing().size();
    }

    @Override
    public long getFollowersCount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return user.getFollowers().size();
    }

    @Override
    public boolean isFollowing(Long followerId, Long followingId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new ResourceNotFoundException("Follower not found with id: " + followerId));

        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new ResourceNotFoundException("User to check not found with id: " + followingId));

        return follower.getFollowing().contains(following);
    }
}
