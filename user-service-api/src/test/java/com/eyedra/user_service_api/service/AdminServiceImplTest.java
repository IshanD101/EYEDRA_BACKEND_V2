package com.eyedra.user_service_api.service;

import com.eyedra.user_service_api.dto.response.UserListResponseDto;
import com.eyedra.user_service_api.entity.User;
import com.eyedra.user_service_api.exception.UserNotFoundException;
import com.eyedra.user_service_api.repository.UserRepository;
import com.eyedra.user_service_api.services.impl.AdminServiceImpl;
import com.eyedra.user_service_api.util.Role;
import com.eyedra.user_service_api.util.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AdminServiceImpl adminService;

    @Test
    void testGetAllUsers() {
        User user = new User();
        user.setUserId(1L);
        user.setUsername("john_doe");
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.mapToUserListDto(user)).thenReturn(new UserListResponseDto(1L, "john_doe", "john@example.com", "John", "Doe", "1234567890", Role.ROLE_USER));

        List<UserListResponseDto> users = adminService.getAllUsers();
        assertEquals(1, users.size());
        assertEquals("john_doe", users.get(0).getUsername());
    }

    @Test
    void testUpdateUserRole_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> adminService.updateUserRole(1L, Role.ROLE_ADMINISTRATOR));
    }

    @Test
    void testDeleteUser_UserNotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);
        assertThrows(UserNotFoundException.class, () -> adminService.deleteUser(1L));
    }

    @Test
    void testDeleteUser_Success() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);
        adminService.deleteUser(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }
}
