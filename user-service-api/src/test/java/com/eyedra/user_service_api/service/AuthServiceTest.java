package com.eyedra.user_service_api.service;

import com.eyedra.user_service_api.dto.request.LoginReqDto;
import com.eyedra.user_service_api.dto.request.RegisterReqDto;
import com.eyedra.user_service_api.dto.response.AuthResponseDto;
import com.eyedra.user_service_api.entity.User;
import com.eyedra.user_service_api.exception.UserNotFoundException;
import com.eyedra.user_service_api.jwt.JwtUtils;
import com.eyedra.user_service_api.repository.UserRepository;
import com.eyedra.user_service_api.services.AuthService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.eyedra.user_service_api.util.Role;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    private User user;


    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("testuser");
        user.setPassword("encodedPassword");
        user.setRole(Role.ROLE_USER);
    }

    @Test
    void testLogin_Success() {
        LoginReqDto loginReq = new LoginReqDto("testuser", "password");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(jwtUtils.generateToken("testuser", Role.ROLE_USER)).thenReturn("mock-jwt-token");

        AuthResponseDto response = authService.login(loginReq);

        assertNotNull(response);
        assertEquals("mock-jwt-token", response.getToken());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void testRegisterUser_Success() {
        RegisterReqDto registerReq = new RegisterReqDto("testuser", "password", "test@example.com", "John", "Doe", "1234567890");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        assertDoesNotThrow(() -> authService.registerUser(registerReq));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testGetUserByUsername_UserNotFound() {
        when(userRepository.findByUsername("unknownUser")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> authService.getUserByUsername("unknownUser"));
    }
}
