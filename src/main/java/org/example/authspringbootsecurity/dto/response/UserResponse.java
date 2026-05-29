package org.example.authspringbootsecurity.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.authspringbootsecurity.model.Role;
import org.example.authspringbootsecurity.model.User;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Set<String> roles;
    private Boolean active;
    private Boolean emailVerified;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;

    public static UserResponse fromEntity(User user) {
        Set<String> roleNames = user.getRoles() != null
                ? user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet())
                : Set.of();
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .roles(roleNames)
                .active(user.getActive())
                .emailVerified(user.getEmailVerified())
                .createdAt(user.getCreatedAt())
                .lastLogin(user.getLastLogin())
                .build();
    }
}
