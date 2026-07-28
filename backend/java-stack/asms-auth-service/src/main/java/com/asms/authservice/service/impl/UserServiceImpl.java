package com.asms.authservice.service.impl;

import com.asms.authservice.dto.ChangePasswordRequest;
import com.asms.authservice.dto.UserProfileResponse;
import com.asms.authservice.entity.Role;
import com.asms.authservice.entity.User;
import com.asms.authservice.exception.BadRequestException;
import com.asms.authservice.exception.ResourceNotFoundException;
import com.asms.authservice.repository.UserRepository;
import com.asms.authservice.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * UserService implementation for profile management and admin operations.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Retrieve the profile of the authenticated user (no password exposed).
     */
    @Override
    @Transactional(readOnly = true)
    public UserProfileResponse getUserProfile(String username) {
        User user = findUserByUsername(username);
        return mapToProfileResponse(user);
    }

    /**
     * Update first name, last name, and/or phone number.
     * Only non-blank values are applied (partial update).
     */
    @Override
    @Transactional
    public UserProfileResponse updateProfile(String username, String firstName,
                                             String lastName, String phoneNo) {
        User user = findUserByUsername(username);

        if (firstName != null && !firstName.isBlank()) {
            user.setFirstName(firstName);
        }
        if (lastName != null && !lastName.isBlank()) {
            user.setLastName(lastName);
        }
        if (phoneNo != null && !phoneNo.isBlank()) {
            user.setPhoneNo(phoneNo);
        }

        return mapToProfileResponse(userRepository.save(user));
    }

    /**
     * Change password after verifying the current password.
     * New password and confirm password must match.
     */
    @Override
    @Transactional
    public void changePassword(String username, ChangePasswordRequest request) {
        User user = findUserByUsername(username);

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password is incorrect");
        }
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("New password and confirm password do not match");
        }
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new BadRequestException("New password must be different from the current password");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    /**
     * Retrieve all registered users — ADMIN only endpoint.
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserProfileResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToProfileResponse)
                .collect(Collectors.toList());
    }

    /**
     * Deactivate a user — ADMIN only.
     * Deactivated users cannot log in or use their tokens.
     */
    @Override
    @Transactional
    public void deactivateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));
        user.setIsActive(false);
        userRepository.save(user);
    }

    /**
     * Re-activate a deactivated user — ADMIN only.
     */
    @Override
    @Transactional
    public void activateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));
        user.setIsActive(true);
        userRepository.save(user);
    }

    // ===================== Helpers =====================

    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found: " + username));
    }

    /**
     * Map User entity to a safe public response (no password field).
     */
    private UserProfileResponse mapToProfileResponse(User user) {
        return UserProfileResponse.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .phoneNo(user.getPhoneNo())
                .createdAt(user.getCreatedAt())
                .isActive(user.getIsActive())
                .roles(user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet()))
                .build();
    }
}
