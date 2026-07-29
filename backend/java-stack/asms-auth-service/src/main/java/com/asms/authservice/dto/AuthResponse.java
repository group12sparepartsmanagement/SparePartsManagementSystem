package com.asms.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Response DTO returned after successful login or registration.
 * Contains both access token (short-lived) and refresh token (long-lived).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    /** JWT access token — valid for 24 hours */
    private String accessToken;

    /** JWT refresh token — valid for 7 days */
    private String refreshToken;

    /** Token type is always "Bearer" */
    @Builder.Default
    private String tokenType = "Bearer";

}