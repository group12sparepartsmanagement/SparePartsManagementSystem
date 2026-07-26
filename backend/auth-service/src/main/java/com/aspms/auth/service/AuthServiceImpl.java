package com.aspms.auth.service;

import com.aspms.auth.dto.AuthResponse;
import com.aspms.auth.dto.LoginRequest;
import com.aspms.auth.dto.RegisterRequest;
import com.aspms.auth.entity.Role;
import com.aspms.auth.entity.User;
import com.aspms.auth.repository.RoleRepository;
import com.aspms.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * AuthServiceImpl — handles register and login business logic.
 *
 * Register: validates username uniqueness → resolves role → BCrypt-encodes password → saves user
 * Login:    loads user by uname → verifies BCrypt password → returns user info
 *
 * NOTE: JWT token generation will be added to this service in a future iteration.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // ===== Register =====

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {

        // 1. Check username is not already taken
        if (userRepository.existsByUname(request.getUname())) {
            throw new RuntimeException("Username already exists: " + request.getUname());
        }

        // 2. Validate the role ID
        Role role = roleRepository.findById(request.getRid())
                .orElseThrow(() -> new RuntimeException(
                        "Invalid role ID: " + request.getRid()
                        + " — valid values: 1=Admin, 2=Supplier, 3=Customer, 4=Staff"));

        // 3. Build user with BCrypt-hashed password
        User user = User.builder()
                .uname(request.getUname())
                .password(passwordEncoder.encode(request.getPassword()))
                .address(request.getAddress())
                .role(role)
                .build();

        // 4. Persist
        User saved = userRepository.save(user);
        log.info("Registered new user: uid={}, uname={}, role={}", saved.getUid(), saved.getUname(), role.getRname());

        return AuthResponse.builder()
                .uid(saved.getUid())
                .uname(saved.getUname())
                .role(role.getRname())
                .address(saved.getAddress())
                .message("Registration successful")
                .build();
    }

    // ===== Login =====

    @Override
    public AuthResponse login(LoginRequest request) {

        // 1. Find user by username
        User user = userRepository.findByUname(request.getUname())
                .orElseThrow(() -> new RuntimeException("User not found: " + request.getUname()));

        // 2. Verify BCrypt password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        log.info("User logged in: uid={}, uname={}", user.getUid(), user.getUname());

        // 3. Return user info
        // TODO: Generate and return JWT token here in the next iteration
        return AuthResponse.builder()
                .uid(user.getUid())
                .uname(user.getUname())
                .role(user.getRole().getRname())
                .address(user.getAddress())
                .message("Login successful")
                .build();
    }
}
