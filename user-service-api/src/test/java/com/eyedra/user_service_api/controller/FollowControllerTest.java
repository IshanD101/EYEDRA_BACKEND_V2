package com.eyedra.user_service_api.controller;

import com.eyedra.user_service_api.dto.response.UserSummaryResponseDto;
import com.eyedra.user_service_api.services.impl.FollowingServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class FollowControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private FollowingServiceImpl followService;

    @InjectMocks
    private FollowController followController;

    public FollowControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(roles = {"USER", "LISTENER"})
    public void testFollowUser() throws Exception {
        doNothing().when(followService).followUser(anyLong(), anyLong());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/user/1/follow")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"USER", "LISTENER"})
    public void testUnfollowUser() throws Exception {
        doNothing().when(followService).unfollowUser(anyLong(), anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/user/1/unfollow")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetFollowing() throws Exception {
        when(followService.getFollowing(anyLong())).thenReturn(List.of(new UserSummaryResponseDto()));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user/1/following")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetFollowers() throws Exception {
        when(followService.getFollowers(anyLong())).thenReturn(List.of(new UserSummaryResponseDto()));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user/1/followers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetFollowingCount() throws Exception {
        when(followService.getFollowingCount(anyLong())).thenReturn(5L);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user/1/following/count")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(5));
    }

    @Test
    public void testGetFollowersCount() throws Exception {
        when(followService.getFollowersCount(anyLong())).thenReturn(3L);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user/1/followers/count")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(3));
    }
}
