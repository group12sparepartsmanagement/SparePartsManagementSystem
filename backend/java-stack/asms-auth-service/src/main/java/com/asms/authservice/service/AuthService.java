package com.asms.authservice.service;

import com.asms.authservice.dto.AuthResponse;
import com.asms.authservice.dto.LoginRequest;
import com.asms.authservice.dto.RefreshTokenRequest;
import com.asms.authservice.dto.RegisterRequest;

/**
 * Authentication service contract.
 * Handles login, registration, token refresh, and logout.
 */
public interface AuthService {

    /**
     * Authenticate user with username/email and password.
     * Returns access and refresh tokens.
     */
    AuthResponse login(LoginRequest loginRequest);

    /**
     * Register a new user and return tokens immediately.
     */
    AuthResponse register(RegisterRequest registerRequest);

    /**
     * Exchange a valid refresh token for new access + refresh tokens.
     * Old refresh token is invalidated after use (rotation).
     */
    AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

    /**
     * Invalidate the given access token (add to blacklist).
     */
    void logout(String token);
}
