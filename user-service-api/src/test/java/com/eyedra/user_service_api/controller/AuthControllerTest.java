package com.eyedra.user_service_api.controller;

import com.eyedra.user_service_api.dto.request.LoginReqDto;
import com.eyedra.user_service_api.dto.request.RegisterReqDto;
import com.eyedra.user_service_api.dto.response.AuthResponseDto;
import com.eyedra.user_service_api.services.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    void testLogin_Success() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();

        LoginReqDto loginReq = new LoginReqDto("testuser", "password");
        AuthResponseDto authResponse = new AuthResponseDto("mock-jwt-token");

        when(authService.login(any(LoginReqDto.class))).thenReturn(authResponse);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testuser\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-jwt-token"));

        verify(authService, times(1)).login(any(LoginReqDto.class));
    }

    @Test
    void testRegister_Success() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();

        RegisterReqDto registerReq = new RegisterReqDto("testuser", "password", "test@example.com", "John", "Doe", "1234567890");

        doNothing().when(authService).registerUser(any(RegisterReqDto.class));

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testuser\",\"password\":\"password\",\"email\":\"test@example.com\",\"firstName\":\"John\",\"lastName\":\"Doe\",\"mobileNumber\":\"1234567890\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("User Registered Successfully....!!!"));

        verify(authService, times(1)).registerUser(any(RegisterReqDto.class));
    }
}
