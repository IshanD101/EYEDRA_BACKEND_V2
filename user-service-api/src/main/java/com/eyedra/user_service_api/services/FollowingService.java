package com.eyedra.user_service_api.services;


import com.eyedra.user_service_api.dto.response.UserSummaryResponseDto;

import java.util.List;

public interface FollowingService {
    void followUser(Long followerId, Long followingId);

    void unfollowUser(Long followerId, Long followingId);

    List<UserSummaryResponseDto> getFollowing(Long userId);

    List<UserSummaryResponseDto> getFollowers(Long userId);

    long getFollowingCount(Long userId);

    long getFollowersCount(Long userId);

    boolean isFollowing(Long followerId, Long followingId);
}
