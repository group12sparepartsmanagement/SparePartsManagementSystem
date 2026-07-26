package com.aspms.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Minimal security configuration.
 *
 * Currently:
 *   - All endpoints are open (no token required)
 *   - CSRF disabled for REST API use
 *   - BCryptPasswordEncoder bean exposed for password hashing
 *
 * When the mentor gives the go-ahead, JWT filter chain
 * and token validation will be added here.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Permit all requests — no authentication enforced at this stage.
     * Security will be layered in once JWT implementation begins.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            );
        return http.build();
    }

    /**
     * BCrypt password encoder — used to hash passwords during registration
     * and verify them during login.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
