package com.eyedra.user_service_api.entity;

import com.eyedra.user_service_api.util.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String firstName;
    private String lastName;

    @Column(nullable = false, unique = true)
    private String username;

    private String email;
    private String password;
    private String mobileNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;  // Store a single role as a column in the User table

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of((GrantedAuthority) role::name); // Convert role to authority
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

