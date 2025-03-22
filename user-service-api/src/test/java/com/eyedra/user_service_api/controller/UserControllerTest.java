package com.eyedra.user_service_api.controller;

import com.eyedra.user_service_api.dto.request.ProfileUpdateReqDto;
import com.eyedra.user_service_api.entity.User;
import com.eyedra.user_service_api.services.AuthService;
import com.eyedra.user_service_api.services.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.http.MediaType;

@ExtendWith(SpringExtension.class)
public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testGetUser_Success() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setFirstName("John");
        user.setLastName("Doe");

        when(authService.getUserByUsername("testuser")).thenReturn(user);

        mockMvc.perform(get("/api/v1/user/testuser")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));

        verify(authService, times(1)).getUserByUsername("testuser");
    }

    @Test
    void testConvertToListener_Success() throws Exception {
        doNothing().when(authService).upgradeToListener("testuser");

        mockMvc.perform(post("/api/v1/user/convert/testuser")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("User testuser upgraded to a listener"));

        verify(authService, times(1)).upgradeToListener("testuser");
    }

    @Test
    void testUpdateUser_Success() throws Exception {
        ProfileUpdateReqDto updateReqDto = new ProfileUpdateReqDto();
        updateReqDto.setUsername("John");
        updateReqDto.setDescription("Updated profile description");
        updateReqDto.setImageUrl("http://example.com/image.jpg");

        when(authentication.getName()).thenReturn("testuser");
        doNothing().when(userService).updateProfile("testuser", updateReqDto);

        mockMvc.perform(put("/api/v1/user/update/testuser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testuser\", \"description\":\"Updated profile description\", \"imageUrl\":\"http://example.com/image.jpg\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("User testuser updated"));

        verify(userService, times(1)).updateProfile("testuser", updateReqDto);
    }

}
