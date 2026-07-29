package com.asms.authservice.config;

import com.asms.authservice.entity.Role;
import com.asms.authservice.entity.User;
import com.asms.authservice.repository.RoleRepository;
import com.asms.authservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Runs on application startup to seed the database with:
 * 1. Four roles: ROLE_ADMIN, ROLE_EMPLOYEE, ROLE_CUSTOMER, ROLE_SUPPLIER
 * 2. A default admin user (username: admin, password: Admin@1234)
 *
 * Idempotent — safe to run on every startup; skips existing data.
 */
@Component
public class DataInitializer implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    /** All role names seeded into the roles table */
    private static final List<String> ROLE_NAMES = List.of(
            "ROLE_ADMIN",
            "ROLE_EMPLOYEE",
            "ROLE_CUSTOMER",
            "ROLE_SUPPLIER"
    );

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        seedRoles();
        seedDefaultAdmin();
    }

    /**
     * Insert each role if it doesn't already exist.
     */
    private void seedRoles() {
        ROLE_NAMES.forEach(roleName -> {
            if (!roleRepository.existsByName(roleName)) {
                Role role = new Role();
                role.setName(roleName);
                roleRepository.save(role);
                logger.info("Seeded role: {}", roleName);
            }
        });
    }

    /**
     * Create a default admin user if none exists.
     * ⚠️  Change the default password before deploying to production!
     */
    private void seedDefaultAdmin() {
        if (!userRepository.existsByUsername("admin")) {
            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseThrow(() -> new RuntimeException("ROLE_ADMIN not found during admin seeding"));

            User admin = User.builder()
                    .firstName("System")
                    .lastName("Admin")
                    .username("admin")
                    .email("admin@asms.com")
                    .password(passwordEncoder.encode("Admin@1234"))
                    .phoneNo("+919999999999")
                    .isActive(true)
                    .roles(new HashSet<>(Set.of(adminRole)))
                    .build();

            userRepository.save(admin);
            logger.info("Default admin created — username: admin  |  password: Admin@1234");
            logger.warn("SECURITY: Change the default admin password before production deployment!");
        }
    }
}