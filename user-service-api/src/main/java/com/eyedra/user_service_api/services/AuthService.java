package com.eyedra.user_service_api.services;

import com.eyedra.user_service_api.dto.request.LoginReqDto;
import com.eyedra.user_service_api.dto.request.RegisterReqDto;
import com.eyedra.user_service_api.dto.response.AuthResponseDto;
import com.eyedra.user_service_api.entity.User;
import com.eyedra.user_service_api.jwt.JwtUtils;
import com.eyedra.user_service_api.repository.UserRepository;
import com.eyedra.user_service_api.util.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Getter
@Setter
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private UserRepository userRepository;

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void upgradeToListener(String username) {
        User user = getUserByUsername(username);
        user.setRole(Role.ROLE_LISTENER);
        userRepository.save(user);
    }

    public void registerUser(RegisterReqDto request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setMobileNumber(request.getMobileNumber());
        user.setRole(Role.ROLE_USER);

        userRepository.save(user);
    }


    public AuthResponseDto login(LoginReqDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate JWT token with role information
        String token = jwtUtils.generateToken(user.getUsername(), user.getRole());

        return new AuthResponseDto(token);
    }
}