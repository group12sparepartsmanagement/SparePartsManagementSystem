package com.asms.authservice.repository;

import com.asms.authservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for User entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by email address.
     */
    Optional<User> findByEmail(String email);

    /**
     * Find user by username.
     */
    Optional<User> findByUsername(String username);

    /**
     * Find user by username OR email — used in login (supports both login methods).
     */
    Optional<User> findByUsernameOrEmail(String username, String email);

    /**
     * Check if an email is already registered.
     */
    boolean existsByEmail(String email);

    /**
     * Check if a username is already taken.
     */
    boolean existsByUsername(String username);
}
