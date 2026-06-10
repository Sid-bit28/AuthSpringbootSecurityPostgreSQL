package org.example.authspringbootsecurity.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenInfoResponse {
    private String username;
    private List<String> roles;
    private Long expiresAt;
    private String tokenType;
}
