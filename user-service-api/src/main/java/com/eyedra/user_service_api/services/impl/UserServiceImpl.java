package com.eyedra.user_service_api.services.impl;

import com.eyedra.user_service_api.dto.request.ProfileUpdateReqDto;
import com.eyedra.user_service_api.entity.User;
import com.eyedra.user_service_api.repository.UserRepository;
import com.eyedra.user_service_api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void updateProfile(String username, ProfileUpdateReqDto updateReqDto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(updateReqDto.getUsername() != null && !updateReqDto.getUsername().isEmpty()) {
            user.setUsername(updateReqDto.getUsername());
        }
        if (updateReqDto.getDescription() != null) {
            user.setDescription(updateReqDto.getDescription());
        }
        if (updateReqDto.getImageUrl() != null) {
            user.setImageUrl(updateReqDto.getImageUrl());
        }

        userRepository.save(user);
    }
}
