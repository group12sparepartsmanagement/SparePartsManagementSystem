package com.aspms.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Request payload for POST /api/auth/register
 *
 * rid must match a valid role from the `role` table:
 *   1 = Admin, 2 = Supplier, 3 = Customer, 4 = Staff
 *
 * Note: Admin role registration should ideally be restricted
 * in production by additional role-based guard.
 */
@Data
public class RegisterRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 100, message = "Username must be between 3 and 100 characters")
    private String uname;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    private String address;

    @NotNull(message = "Role ID is required (1=Admin, 2=Supplier, 3=Customer, 4=Staff)")
    private Integer rid;
}
