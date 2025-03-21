package com.eyedra.user_service_api.entity;

import com.eyedra.user_service_api.util.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
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

    private String description;
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;  // Store a single role as a column in the User table

    @ManyToMany
    @JoinTable(
            name = "user_following",
            joinColumns = @JoinColumn(name = "follower_id"),
            inverseJoinColumns = @JoinColumn(name = "following_id")
    )
    private Set<User> following = new HashSet<>();

    @ManyToMany(mappedBy = "following")
    private Set<User> followers = new HashSet<>();

    public void follow(User userToFollow) {
        following.add(userToFollow);
    }

    public void unfollow(User userToUnfollow) {
        following.remove(userToUnfollow);
    }

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

