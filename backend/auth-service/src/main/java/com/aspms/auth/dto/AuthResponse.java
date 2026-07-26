package com.aspms.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response returned after a successful register or login.
 *
 * Contains basic user identity info.
 * Token/JWT fields will be added later when the security layer is implemented.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponse {

    private Integer uid;
    private String uname;
    private String role;
    private String address;
    private String message;
}
