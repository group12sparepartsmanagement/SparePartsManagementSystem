package com.aspms.auth.repository;

import com.aspms.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * JPA repository for the User entity.
 * Provides login lookup by username and username uniqueness check.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Used by Spring Security's UserDetailsService and the login flow
     * to load a user by their username.
     */
    Optional<User> findByUname(String uname);

    /**
     * Used during registration to prevent duplicate usernames.
     */
    boolean existsByUname(String uname);
}
