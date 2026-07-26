package com.amsps.auth.service;

import com.amsps.auth.dto.AuthResponse;
import com.amsps.auth.dto.LoginRequest;
import com.amsps.auth.dto.RegisterRequest;
import com.amsps.auth.entity.Role;
import com.amsps.auth.entity.User;
import com.amsps.auth.repository.RoleRepository;
import com.amsps.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private  UserRepository userRepository;
    private  RoleRepository roleRepository;
    private  PasswordEncoder passwordEncoder;

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
