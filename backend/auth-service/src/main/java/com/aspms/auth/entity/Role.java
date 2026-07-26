package com.aspms.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Maps the `role` table in the aspmsnew database.
 *
 * Pre-seeded roles:
 *   rid=1 → Admin
 *   rid=2 → Supplier
 *   rid=3 → Customer
 *   rid=4 → Staff
 */
@Entity
@Table(name = "role")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rid")
    private Integer rid;

    @Column(name = "rname", nullable = false, unique = true, length = 50)
    private String rname;
}
