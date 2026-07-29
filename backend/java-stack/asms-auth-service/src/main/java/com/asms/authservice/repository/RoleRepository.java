package com.asms.authservice.repository;

import com.asms.authservice.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Role entity.
 * Used for seeding roles and looking them up during user registration.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Find a role by its name (e.g. "ROLE_ADMIN", "ROLE_EMPLOYEE").
     */
    Optional<Role> findByName(String name);

    /**
     * Check if a role with the given name already exists (used by DataInitializer).
     */
    boolean existsByName(String name);
}
