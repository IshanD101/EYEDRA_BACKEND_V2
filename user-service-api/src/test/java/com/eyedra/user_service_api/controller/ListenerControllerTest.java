package com.eyedra.user_service_api.controller;

import com.eyedra.user_service_api.dto.request.ListenerProfileDto;
import com.eyedra.user_service_api.dto.request.ListenerProfileUpdateReqDto;
import com.eyedra.user_service_api.dto.request.ListenerStatsDto;
import com.eyedra.user_service_api.services.impl.ListenerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ListenerController.class)
public class ListenerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ListenerServiceImpl listenerService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ListenerController listenerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testListenerDashboard() throws Exception {
        mockMvc.perform(get("/api/v1/listener/lDashboard"))
                .andExpect(status().isOk())
                .andExpect(content().string("Welcome to Listener dashboard!!!!!!"))
                .andDo(print());
    }

    @Test
    public void testGetListenerProfile() throws Exception {
        ListenerProfileDto profileDto = new ListenerProfileDto();
        profileDto.setUsername("listener1");

        when(authentication.getName()).thenReturn("listener1");
        when(listenerService.getListenerProfile("listener1")).thenReturn(profileDto);

        mockMvc.perform(get("/api/v1/listener/profile")
                        .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("listener1"))
                .andDo(print());
    }

    @Test
    public void testUpdateListenerProfile() throws Exception {
        ListenerProfileUpdateReqDto updateDto = new ListenerProfileUpdateReqDto();
        updateDto.setDescription("Updated Description");
        updateDto.setImageUrl("http://example.com/image.jpg");

        when(authentication.getName()).thenReturn("listener1");
        doNothing().when(listenerService).updateListenerProfile(eq("listener1"), any(ListenerProfileUpdateReqDto.class));

        mockMvc.perform(put("/api/v1/listener/profile")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\":\"Updated Description\", \"imageUrl\":\"http://example.com/image.jpg\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Listener profile updated successfully"))
                .andDo(print());
    }

    @Test
    public void testGetListenerStats() throws Exception {
        ListenerStatsDto statsDto = ListenerStatsDto.builder()
                .totalSessions(10)
                .build();

        when(authentication.getName()).thenReturn("listener1");
        when(listenerService.getListenerStats("listener1")).thenReturn(statsDto);

        mockMvc.perform(get("/api/v1/listener/stats")
                        .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalSessions").value(10))
                .andDo(print());
    }
}
