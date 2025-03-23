package com.eyedra.user_service_api.service;

import com.eyedra.user_service_api.entity.User;
import com.eyedra.user_service_api.repository.UserRepository;
import com.eyedra.user_service_api.services.impl.FollowingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FollowServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FollowingServiceImpl followingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFollowUser() {
        User user = new User();
        User userToFollow = new User();
        user.setUserId(1L);
        userToFollow.setUserId(2L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(userToFollow));

        followingService.followUser(1L, 2L);

        assertTrue(user.getFollowing().contains(userToFollow));
        assertTrue(userToFollow.getFollowers().contains(user));
        verify(userRepository, times(1)).save(user);
        verify(userRepository, times(1)).save(userToFollow);
    }

    @Test
    void testUnfollowUser() {
        User user = new User();
        User userToUnfollow = new User();
        user.setUserId(1L);
        userToUnfollow.setUserId(2L);
        user.getFollowing().add(userToUnfollow);
        userToUnfollow.getFollowers().add(user);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(userToUnfollow));

        followingService.unfollowUser(1L, 2L);

        assertFalse(user.getFollowing().contains(userToUnfollow));
        assertFalse(userToUnfollow.getFollowers().contains(user));
        verify(userRepository, times(1)).save(user);
        verify(userRepository, times(1)).save(userToUnfollow);
    }

    @Test
    void testGetFollowingCount() {
        User user = new User();
        Set<User> following = new HashSet<>();
        following.add(new User());
        following.add(new User());
        user.setFollowing(following);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        long count = followingService.getFollowingCount(1L);

        assertEquals(2L, count);
    }

    @Test
    void testGetFollowersCount() {
        User user = new User();
        Set<User> followers = new HashSet<>();
        followers.add(new User());
        user.setFollowers(followers);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        long count = followingService.getFollowersCount(1L);

        assertEquals(1L, count);
    }

}
