package com.asms.authservice.security;

import com.asms.authservice.entity.User;
import com.asms.authservice.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Spring Security UserDetailsService implementation.
 *
 * Loads user by username or email (supporting both login methods).
 * Maps Role entities to Spring Security GrantedAuthority objects.
 * Checks isActive flag — deactivated users cannot authenticate.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads user by username or email.
     * Called by Spring Security during authentication.
     *
     * @param usernameOrEmail the username or email entered at login
     * @return populated UserDetails for authentication
     * @throws UsernameNotFoundException if user not found or is deactivated
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with username or email: " + usernameOrEmail));

        if (!Boolean.TRUE.equals(user.getIsActive())) {
            throw new UsernameNotFoundException(
                    "User account is deactivated: " + usernameOrEmail);
        }

        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }
}
