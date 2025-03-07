package com.eyedra.user_service_api.services;

import com.eyedra.user_service_api.dto.request.ProfileUpdateReqDto;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    void updateProfile(String username, ProfileUpdateReqDto profileUpdateReqDto);
}
