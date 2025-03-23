package com.eyedra.user_service_api.repository;

import com.eyedra.user_service_api.entity.User;
import com.eyedra.user_service_api.util.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByUsername_UserExists() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("encodedPassword");
        user.setEmail("test@example.com");
        user.setRole(Role.ROLE_USER);
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByUsername("testuser");
        assertTrue(foundUser.isPresent());
        assertEquals("testuser", foundUser.get().getUsername());
    }

    @Test
    void testFindByUsername_UserNotFound() {
        Optional<User> foundUser = userRepository.findByUsername("nonexistentUser");
        assertFalse(foundUser.isPresent());
    }

    @Test
    void testExistsByUsername() {
        User user = new User();
        user.setUsername("testuser");
        userRepository.save(user);

        assertTrue(userRepository.existsByUsername("testuser"));
        assertFalse(userRepository.existsByUsername("unknownUser"));
    }
}
