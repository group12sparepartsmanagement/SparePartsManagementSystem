package com.asms.authservice.controller;

import com.asms.authservice.dto.*;
import com.asms.authservice.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Public authentication endpoints — no JWT required.
 *
 * Endpoints:
 * POST /api/auth/register — Register a new user
 * POST /api/auth/login — Login and receive tokens
 * POST /api/auth/refresh — Refresh expired access token
 * POST /api/auth/logout — Invalidate current token
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Register a new user.
     *
     * Request body:
     * {
     * "firstName": "John",
     * "lastName": "Doe",
     * "username": "johndoe",
     * "email": "john@example.com",
     * "password": "Secret@123",
     * "phoneNo": "+911234567890",
     * "roleName": "ROLE_CUSTOMER"
     * }
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest registerRequest) {

        AuthResponse authResponse = authService.register(registerRequest);
        return ResponseEntity.ok(ApiResponse.success("Registration successful", authResponse));
    }

    /**
     * Login with username/email and password.
     *
     * Request body:
     * {
     * "usernameOrEmail": "johndoe",
     * "password": "Secret@123"
     * }
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest loginRequest) {

        AuthResponse authResponse = authService.login(loginRequest);
        return ResponseEntity.ok(ApiResponse.success("Login successful", authResponse));
    }

    /**
     * Exchange a refresh token for a new pair of tokens.
     *
     * Request body:
     * {
     * "refreshToken": "<your-refresh-token>"
     * }
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {

        AuthResponse authResponse = authService.refreshToken(refreshTokenRequest);
        return ResponseEntity.ok(ApiResponse.success("Token refreshed successfully", authResponse));
    }

    /**
     * Logout — blacklists the current access token.
     * Client must also discard the refresh token locally.
     *
     * Header: Authorization: Bearer <access-token>
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        String token = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }
        authService.logout(token);
        return ResponseEntity.ok(ApiResponse.success("Logged out successfully", null));
    }
}