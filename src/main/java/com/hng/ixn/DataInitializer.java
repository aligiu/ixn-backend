package com.hng.ixn;

import com.hng.ixn.user.Role;
import com.hng.ixn.user.User;
import com.hng.ixn.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByEmail("admin@example.com").isEmpty()) {
            User admin = User.builder()
                    .email("admin@example.com")
                    .password(passwordEncoder.encode("SecurePassword123"))
                    .role(Role.ROLE_ADMIN)
                    .build();
            userRepository.save(admin);
        }
        // second admin with username and password as "a" for fast testing
        if (userRepository.findByEmail("a").isEmpty()) {
            User admin = User.builder()
                    .email("a")
                    .password(passwordEncoder.encode("a"))
                    .role(Role.ROLE_ADMIN)
                    .build();
            userRepository.save(admin);
        }
    }
}
