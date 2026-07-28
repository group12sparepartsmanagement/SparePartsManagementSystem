package com.asms.authservice.service.impl;

import com.asms.authservice.dto.AuthResponse;
import com.asms.authservice.dto.LoginRequest;
import com.asms.authservice.dto.RefreshTokenRequest;
import com.asms.authservice.dto.RegisterRequest;
import com.asms.authservice.entity.Role;
import com.asms.authservice.entity.User;
import com.asms.authservice.exception.BadRequestException;
import com.asms.authservice.exception.ResourceNotFoundException;
import com.asms.authservice.repository.RoleRepository;
import com.asms.authservice.repository.UserRepository;
import com.asms.authservice.security.JwtTokenProvider;
import com.asms.authservice.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * AuthService implementation.
 *
 * Token strategy:
 * - Access token (24h): used for every API request
 * - Refresh token (7d): used only to obtain a new access token
 * - Token rotation: each refresh invalidates the old refresh token
 * - Logout: adds token to in-memory blacklist
 *
 * Note: Token blacklist uses ConcurrentHashMap — replace with Redis for production
 *       to support distributed deployments and persistence across restarts.
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    // Thread-safe in-memory blacklist for invalidated tokens
    private final Set<String> tokenBlacklist = ConcurrentHashMap.newKeySet();

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Authenticate user credentials and return JWT tokens.
     */
    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest loginRequest) {
        // Delegates to CustomUserDetailsService + BCrypt verification
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByUsernameOrEmail(
                loginRequest.getUsernameOrEmail(),
                loginRequest.getUsernameOrEmail()
        ).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Set<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        String accessToken  = jwtTokenProvider.generateAccessToken(authentication, user.getUserId(), user.getEmail(), roles);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication.getName());

        return buildAuthResponse(accessToken, refreshToken);
    }

    /**
     * Register a new user and immediately return JWT tokens.
     */
    @Override
    @Transactional
    public AuthResponse register(RegisterRequest registerRequest) {
        // Validate uniqueness
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new BadRequestException(
                    "Email is already registered: " + registerRequest.getEmail());
        }
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new BadRequestException(
                    "Username is already taken: " + registerRequest.getUsername());
        }

        // Validate role exists
        Role role = roleRepository.findByName(registerRequest.getRoleName())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Role not found: '" + registerRequest.getRoleName() +
                        "'. Valid roles: ROLE_ADMIN, ROLE_EMPLOYEE, ROLE_CUSTOMER, ROLE_SUPPLIER"));

        // Build and save user
        User user = User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .phoneNo(registerRequest.getPhoneNo())
                .roles(new HashSet<>(Set.of(role)))
                .build();

        User savedUser = userRepository.save(user);

        // Authenticate to generate tokens
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        registerRequest.getUsername(),
                        registerRequest.getPassword()
                )
        );

        String accessToken  = jwtTokenProvider.generateAccessToken(authentication, savedUser.getUserId(), savedUser.getEmail(), Set.of(role.getName()));
        String refreshToken = jwtTokenProvider.generateRefreshToken(savedUser.getUsername());

        return buildAuthResponse(accessToken, refreshToken);
    }

    /**
     * Refresh tokens using a valid refresh token.
     * Rotates the refresh token — old one is blacklisted.
     */
    @Override
    @Transactional(readOnly = true)
    public AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String incomingRefreshToken = refreshTokenRequest.getRefreshToken();

        if (tokenBlacklist.contains(incomingRefreshToken)) {
            throw new BadRequestException("Refresh token has been invalidated. Please log in again.");
        }
        if (!jwtTokenProvider.validateToken(incomingRefreshToken)) {
            throw new BadRequestException("Refresh token is invalid or has expired. Please log in again.");
        }
        if (!jwtTokenProvider.isRefreshToken(incomingRefreshToken)) {
            throw new BadRequestException("The provided token is not a refresh token.");
        }

        String username = jwtTokenProvider.getUsernameFromToken(incomingRefreshToken);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        if (!Boolean.TRUE.equals(user.getIsActive())) {
            throw new BadRequestException("User account is deactivated. Please contact support.");
        }

        // Rotate: blacklist old refresh token
        tokenBlacklist.add(incomingRefreshToken);

        // Build authority set for new access token
        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                username,
                null,
                roleNames.stream()
                         .map(SimpleGrantedAuthority::new)
                         .collect(Collectors.toSet())
        );

        String newAccessToken  = jwtTokenProvider.generateAccessToken(auth, user.getUserId(), user.getEmail(), roleNames);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(username);

        return buildAuthResponse(newAccessToken, newRefreshToken);
    }

    /**
     * Invalidate a token by adding it to the blacklist.
     */
    @Override
    public void logout(String token) {
        if (token != null && jwtTokenProvider.validateToken(token)) {
            tokenBlacklist.add(token);
        }
    }

    // ===================== Helper =====================

    private AuthResponse buildAuthResponse(String accessToken, String refreshToken) {
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .build();
    }
}
