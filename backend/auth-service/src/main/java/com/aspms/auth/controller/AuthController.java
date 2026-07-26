package com.aspms.auth.controller;

import com.aspms.auth.dto.ApiResponse;
import com.aspms.auth.dto.AuthResponse;
import com.aspms.auth.dto.LoginRequest;
import com.aspms.auth.dto.RegisterRequest;
import com.aspms.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * AuthController — REST endpoints for authentication.
 *
 * Endpoints:
 *   POST /api/auth/register — Register a new user
 *   POST /api/auth/login    — Login with username and password
 *
 * Future endpoints (added when JWT is implemented):
 *   GET  /api/auth/me       — Get current user profile
 *   GET  /api/auth/validate — Token introspection for other microservices
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Register a new user.
     *
     * Request body:
     *   {
     *     "uname":   "testuser",
     *     "password": "secret123",
     *     "address":  "Pune, Maharashtra",  // optional
     *     "rid":      3                     // 1=Admin, 2=Supplier, 3=Customer, 4=Staff
     *   }
     *
     * Returns 201 Created with basic user info on success.
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request) {

        AuthResponse authResponse = authService.register(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("User registered successfully", authResponse));
    }

    /**
     * Login with username and password.
     *
     * Request body:
     *   {
     *     "uname":    "testuser",
     *     "password": "secret123"
     *   }
     *
     * Returns 200 OK with user info on success.
     * Returns 400 Bad Request if credentials are wrong.
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        AuthResponse authResponse = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful", authResponse));
    }
}
