package com.asms.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Safe user profile response — never exposes the password field.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {

    private Long userId;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String phoneNo;
    private LocalDateTime createdAt;
    private Boolean isActive;

    /** Roles assigned to this user (e.g. ["ROLE_CUSTOMER"]) */
    private Set<String> roles;
}