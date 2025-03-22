package com.eyedra.user_service_api.service;

import com.eyedra.user_service_api.dto.request.ListenerProfileDto;
import com.eyedra.user_service_api.dto.request.ListenerProfileUpdateReqDto;
import com.eyedra.user_service_api.dto.request.ListenerStatsDto;
import com.eyedra.user_service_api.entity.User;
import com.eyedra.user_service_api.exception.NotAListenerException;
import com.eyedra.user_service_api.repository.UserRepository;
import com.eyedra.user_service_api.services.impl.ListenerServiceImpl;
import com.eyedra.user_service_api.util.ListenerProfileMapper;
import com.eyedra.user_service_api.util.Role;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ListenerServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private ListenerProfileMapper listenerProfileMapper;

    @InjectMocks
    private ListenerServiceImpl listenerService;

    @Test
    void testGetListenerProfile_Success() {
        User user = new User();
        user.setUsername("testuser");
        user.setRole(Role.ROLE_LISTENER);

        ListenerProfileDto profileDto = new ListenerProfileDto();
        profileDto.setUsername("testuser");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(listenerProfileMapper.mapToListenerProfileDto(user)).thenReturn(profileDto);

        ListenerProfileDto result = listenerService.getListenerProfile("testuser");

        assertEquals("testuser", result.getUsername());
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void testGetListenerProfile_NotAListener() {
        User user = new User();
        user.setUsername("testuser");
        user.setRole(Role.ROLE_USER);

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        assertThrows(NotAListenerException.class, () -> listenerService.getListenerProfile("testuser"));
    }

    @Test
    void testUpdateListenerProfile_Success() {
        User user = new User();
        user.setUsername("testuser");
        user.setRole(Role.ROLE_LISTENER);

        ListenerProfileUpdateReqDto updateDto = new ListenerProfileUpdateReqDto();
        updateDto.setDescription("New Description");
        updateDto.setImageUrl("http://example.com/new.jpg");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        listenerService.updateListenerProfile("testuser", updateDto);

        assertEquals("New Description", user.getDescription());
        assertEquals("http://example.com/new.jpg", user.getImageUrl());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testGetListenerStats_Success() {
        User user = new User();
        user.setUsername("testuser");
        user.setRole(Role.ROLE_LISTENER);

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        ListenerStatsDto result = listenerService.getListenerStats("testuser");

        assertNotNull(result);
        assertEquals(10, result.getTotalSessions());
        verify(userRepository, times(1)).findByUsername("testuser");
    }
}
