package com.amsps.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *   uid      — PK, auto-increment
 *   rid      — FK → role(rid)
 *   uname    — login username
 *   password — BCrypt hashed password
 *   address  — optional text
 */


@Entity
@Table(name = "user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uid")
    private Integer uid;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rid", nullable = false)
    private Role role;

    @Column(name = "uname", nullable = false, length = 100)
    private String uname;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;
}
