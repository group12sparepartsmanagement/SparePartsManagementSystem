package com.asms.authservice.config;

import com.asms.authservice.security.JwtAccessDeniedHandler;
import com.asms.authservice.security.JwtAuthenticationEntryPoint;
import com.asms.authservice.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Spring Security configuration for the ASMS Auth Service.
 *
 * Key design decisions:
 * - Stateless session (JWT — no server-side session storage)
 * - CSRF disabled (safe for stateless REST APIs)
 * - CORS enabled to support frontend microservices/SPA clients
 * - @EnableMethodSecurity enables @PreAuthorize at method level
 * - Uses DaoAuthenticationProvider with BCrypt
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                          UserDetailsService userDetailsService,
                          JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                          JwtAccessDeniedHandler jwtAccessDeniedHandler) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF — not needed for stateless REST APIs
            .csrf(AbstractHttpConfigurer::disable)

            // Enable CORS with our configuration
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // Stateless session — Spring Security will never create HTTP session
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Authorization rules
            .authorizeHttpRequests(auth -> auth
                // Public auth endpoints — no token required
                .requestMatchers("/api/auth/**").permitAll()

                // Admin-only: list all users
                .requestMatchers(HttpMethod.GET, "/api/users").hasAuthority("ROLE_ADMIN")

                // Admin-only: activate / deactivate users
                .requestMatchers(HttpMethod.PUT, "/api/users/*/activate").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/users/*/deactivate").hasAuthority("ROLE_ADMIN")

                // Authenticated users: own profile and password change
                .requestMatchers("/api/users/profile").authenticated()
                .requestMatchers("/api/users/change-password").authenticated()

                // Everything else also requires authentication
                .anyRequest().authenticated()
            )

            // Custom error responses (JSON instead of HTML)
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)
            )

            // Use our DaoAuthenticationProvider
            .authenticationProvider(authenticationProvider())

            // Add JWT filter BEFORE Spring's default username/password filter
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * DaoAuthenticationProvider wires together UserDetailsService and PasswordEncoder.
     * Spring Security calls this when authenticating login requests.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * Exposes the AuthenticationManager bean — required by AuthServiceImpl for programmatic login.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * BCrypt password encoder with default strength (10 rounds).
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * CORS configuration — allows all origins (restrict in production).
     * Allows the standard auth headers and exposes the Authorization header.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(
                Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(
                Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}