package com.asms.authservice.service;

import com.asms.authservice.dto.ChangePasswordRequest;
import com.asms.authservice.dto.UserProfileResponse;

import java.util.List;

/**
 * User management service contract.
 * Handles profile operations and admin user management.
 */
public interface UserService {

    /**
     * Get profile of the currently authenticated user.
     */
    UserProfileResponse getUserProfile(String username);

    /**
     * Update first name, last name, and phone number for the given user.
     */
    UserProfileResponse updateProfile(String username, String firstName,
                                      String lastName, String phoneNo);

    /**
     * Change password after verifying the current password.
     */
    void changePassword(String username, ChangePasswordRequest request);

    /**
     * Get all users — ADMIN only.
     */
    List<UserProfileResponse> getAllUsers();

    /**
     * Deactivate a user account — ADMIN only.
     * Deactivated users cannot log in.
     */
    void deactivateUser(Long userId);

    /**
     * Re-activate a previously deactivated user — ADMIN only.
     */
    void activateUser(Long userId);
}
