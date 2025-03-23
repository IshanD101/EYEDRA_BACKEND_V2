package com.eyedra.user_service_api.controller;

import com.eyedra.user_service_api.dto.request.UserRoleUpdateDto;
import com.eyedra.user_service_api.dto.response.UserListResponseDto;
import com.eyedra.user_service_api.services.AdminService;
import com.eyedra.user_service_api.util.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
@ExtendWith(SpringExtension.class)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private AdminService adminService;

    @Test
    void testAdminDashboard() throws Exception {
        mockMvc.perform(get("/api/v1/admin/aDashboard"))
                .andExpect(status().isOk())
                .andExpect(content().string("Welcome to the Admin Dashboard!"));
    }

    @Test
    void testGetAllUsers() throws Exception {
        UserListResponseDto user = UserListResponseDto.builder()
                .userId(1L)
                .username("john_doe")
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .role(Role.ROLE_USER)
                .build();

        List<UserListResponseDto> users = List.of(user);
        when(adminService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/v1/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("john_doe"));
    }

    @Test
    void testUpdateUserRole() throws Exception {
        UserRoleUpdateDto roleUpdateDto = new UserRoleUpdateDto(1L, Role.ROLE_ADMINISTRATOR);
        doNothing().when(adminService).updateUserRole(1L, Role.ROLE_ADMINISTRATOR);

        mockMvc.perform(put("/api/v1/admin/user/1/role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"role\": \"ROLE_ADMINISTRATOR\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("User role updated successfully"));
    }

    @Test
    void testDeleteUser() throws Exception {
        doNothing().when(adminService).deleteUser(1L);

        mockMvc.perform(delete("/api/v1/admin/user/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully"));
    }
}
