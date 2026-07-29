package com.asms.authservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Role entity — stores roles like ROLE_ADMIN, ROLE_EMPLOYEE, ROLE_CUSTOMER, ROLE_SUPPLIER.
 * Role names are plain Strings mapped to the 'roles' table.
 */
@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    /**
     * Role name — one of: ROLE_ADMIN, ROLE_EMPLOYEE, ROLE_CUSTOMER, ROLE_SUPPLIER
     */
    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<User> users = new HashSet<>();
}
