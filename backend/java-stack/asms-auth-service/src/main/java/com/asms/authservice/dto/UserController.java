package com.asms.authservice.controller;

import com.asms.authservice.dto.ApiResponse;
import com.asms.authservice.dto.ChangePasswordRequest;
import com.asms.authservice.dto.UserProfileResponse;
import com.asms.authservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * User management endpoints.
 *
 * Public endpoints:   None (all require authentication)
 * User endpoints:     GET/PUT /api/users/profile, POST /api/users/change-password
 * Admin endpoints:    GET /api/users, PUT /api/users/{id}/activate|deactivate
 *
 * @AuthenticationPrincipal injects the currently logged-in UserDetails from SecurityContext.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ===================== User Endpoints =====================

    /**
     * Get the profile of the currently authenticated user.
     * Header: Authorization: Bearer <access-token>
     */
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getMyProfile(
            @AuthenticationPrincipal UserDetails userDetails) {

        UserProfileResponse profile = userService.getUserProfile(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Profile retrieved successfully", profile));
    }

    /**
     * Update the authenticated user's profile (partial update).
     *
     * Request body (all fields optional):
     * {
     *   "firstName": "Jane",
     *   "lastName": "Smith",
     *   "phoneNo": "+911234567890"
     * }
     */
    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<UserProfileResponse>> updateMyProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody Map<String, String> updateRequest) {

        UserProfileResponse profile = userService.updateProfile(
                userDetails.getUsername(),
                updateRequest.get("firstName"),
                updateRequest.get("lastName"),
                updateRequest.get("phoneNo")
        );
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", profile));
    }

    /**
     * Change the authenticated user's password.
     *
     * Request body:
     * {
     *   "currentPassword": "OldPass@1",
     *   "newPassword": "NewPass@2",
     *   "confirmPassword": "NewPass@2"
     * }
     */
    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ChangePasswordRequest request) {

        userService.changePassword(userDetails.getUsername(), request);
        return ResponseEntity.ok(ApiResponse.success("Password changed successfully", null));
    }

    // ===================== Admin Endpoints =====================

    /**
     * Get all registered users. ADMIN only.
     */
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<List<UserProfileResponse>>> getAllUsers() {
        List<UserProfileResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success(
                "Retrieved " + users.size() + " users", users));
    }

    /**
     * Deactivate a user account. ADMIN only.
     * Deactivated users cannot log in.
     *
     * @param id the userId to deactivate
     */
    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deactivateUser(@PathVariable Long id) {
        userService.deactivateUser(id);
        return ResponseEntity.ok(ApiResponse.success(
                "User with id " + id + " has been deactivated", null));
    }

    /**
     * Activate a previously deactivated user. ADMIN only.
     *
     * @param id the userId to activate
     */
    @PutMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> activateUser(@PathVariable Long id) {
        userService.activateUser(id);
        return ResponseEntity.ok(ApiResponse.success(
                "User with id " + id + " has been activated", null));
    }
}