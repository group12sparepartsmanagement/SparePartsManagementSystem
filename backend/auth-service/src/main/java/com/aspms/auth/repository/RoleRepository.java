package com.aspms.auth.repository;

import com.aspms.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * JPA repository for the Role entity.
 * Used during registration to resolve a role by ID or name.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByRname(String rname);
}
