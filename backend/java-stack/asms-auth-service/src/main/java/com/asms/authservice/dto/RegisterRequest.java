package com.asms.authservice.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * Request DTO for user registration.
 * roleName must be one of: ROLE_ADMIN, ROLE_EMPLOYEE, ROLE_CUSTOMER, ROLE_SUPPLIER
 */
@Data
public class RegisterRequest {

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @Pattern(
        regexp = "^[+]?[0-9]{10,15}$",
        message = "Please provide a valid phone number (10-15 digits, optional leading +)"
    )
    private String phoneNo;

    /**
     * Role to assign — must match an existing role name in the database.
     * Valid values: ROLE_ADMIN, ROLE_EMPLOYEE, ROLE_CUSTOMER, ROLE_SUPPLIER
     */
    @NotBlank(message = "Role is required")
    private String roleName;
}