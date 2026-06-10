package org.example.authspringbootsecurity.dto.response;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private Boolean success;
    private String message;
    private Integer statusCode;
    private Object data;
}
