package com.myapp.poc.auth.repository;

import com.myapp.poc.auth.entity.Role;
import com.myapp.poc.auth.entity.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        Role userRole = roleRepository.findByName("USER")
                .orElseGet(() -> roleRepository.save(new Role("USER")));
        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseGet(() -> roleRepository.save(new Role("ADMIN")));

        if (!userRepository.findByUsername("user").isPresent()) {
            User user = new User("user", passwordEncoder.encode("user123"));
            user.addRole(userRole);
            userRepository.save(user);
        }

        if (!userRepository.findByUsername("admin").isPresent()) {
            User admin = new User("admin", passwordEncoder.encode("admin123"));
            admin.addRole(adminRole);
            userRepository.save(admin);
        }
    }
}
