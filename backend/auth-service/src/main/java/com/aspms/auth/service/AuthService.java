package com.aspms.auth.service;

import com.aspms.auth.dto.AuthResponse;
import com.aspms.auth.dto.LoginRequest;
import com.aspms.auth.dto.RegisterRequest;

/**
 * AuthService — contract for authentication operations.
 *
 * Current scope: register + login only.
 * JWT token generation will be added here in a future iteration.
 */
public interface AuthService {

    /**
     * Register a new user.
     * Encodes their password with BCrypt and persists to the `user` table.
     *
     * @throws RuntimeException if username already exists or role ID is invalid
     */
    AuthResponse register(RegisterRequest request);

    /**
     * Login with username and password.
     * Verifies password against the stored BCrypt hash.
     *
     * @throws RuntimeException if user not found or password does not match
     */
    AuthResponse login(LoginRequest request);
}
