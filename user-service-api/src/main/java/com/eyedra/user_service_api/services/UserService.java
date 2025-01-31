package com.eyedra.user_service_api.services;

import com.eyedra.user_service_api.entity.User;
import com.eyedra.user_service_api.repository.UserRepository;
import com.eyedra.user_service_api.util.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import java.util.Set;

@Service
@AllArgsConstructor
@Getter
@Setter
public class UserService {

    private UserRepository userRepository;

    public User getUserByUsername(String username) {
        return userRepository.findByUserName(username).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void upgradeToListener(String username) {
        User user = getUserByUsername(username);
        Set<Role> roles = user.getRoles();
        roles.add(Role.LISTENER);
        userRepository.save(user);
    }

}
