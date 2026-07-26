package com.aspms.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Request payload for POST /api/auth/login
 */
@Data
public class LoginRequest {

    @NotBlank(message = "Username is required")
    private String uname;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
}
