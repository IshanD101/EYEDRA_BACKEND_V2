package com.eyedra.user_service_api.services.impl;

import com.eyedra.user_service_api.dto.request.ListenerProfileDto;
import com.eyedra.user_service_api.dto.request.ListenerProfileUpdateReqDto;
import com.eyedra.user_service_api.dto.request.ListenerStatsDto;
import com.eyedra.user_service_api.entity.User;
import com.eyedra.user_service_api.exception.NotAListenerException;
import com.eyedra.user_service_api.exception.UserNotFoundException;
import com.eyedra.user_service_api.repository.UserRepository;
import com.eyedra.user_service_api.services.ListenerService;
import com.eyedra.user_service_api.util.ListenerProfileMapper;
import com.eyedra.user_service_api.util.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListenerServiceImpl implements ListenerService {

    private final UserRepository userRepository;

    private final ListenerProfileMapper listenerProfileMapper;

    @Override
    public ListenerProfileDto getListenerProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (user.getRole() != Role.ROLE_LISTENER) {
            throw new NotAListenerException("User is not a listener");
        }

        return listenerProfileMapper.mapToListenerProfileDto(user);
    }

    @Override
    public void updateListenerProfile(String username, ListenerProfileUpdateReqDto updateDto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (user.getRole() != Role.ROLE_LISTENER) {
            throw new NotAListenerException("User is not a listener");
        }

        if (updateDto.getDescription() != null) {
            user.setDescription(updateDto.getDescription());
        }

        if (updateDto.getImageUrl() != null) {
            user.setImageUrl(updateDto.getImageUrl());
        }

        userRepository.save(user);
    }

    @Override
    public ListenerStatsDto getListenerStats(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (user.getRole() != Role.ROLE_LISTENER) {
            throw new NotAListenerException("User is not a listener");
        }

        // In a real application, fetch these stats from your database
        return ListenerStatsDto.builder()
                .totalSessions(10)
                .build();
    }
}
