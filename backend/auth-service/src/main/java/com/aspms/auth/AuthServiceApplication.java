package com.aspms.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ASPMS Auth Service — Microservice entry point.
 *
 * Responsibilities:
 *  - User registration and login
 *  - JWT token issuance and validation
 *  - Role-based access control (Admin, Supplier, Customer, Staff)
 *  - Token introspection endpoint for other microservices
 *
 * Runs on port 8081 (configured in application.properties)
 */
@SpringBootApplication
public class AuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }
}
