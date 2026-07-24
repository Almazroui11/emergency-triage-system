package com.triage.emergency_triage_system.config;

import com.triage.emergency_triage_system.model.User;
import com.triage.emergency_triage_system.repo.UserRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        User existing = userRepo.findByUsername("admin").orElse(null);

        if (existing == null) {
            User u = new User();
            u.setName("Administrator");
            u.setEmail("admin@triage.com");
            u.setUsername("admin");
            u.setPassword(passwordEncoder.encode("1234"));
            u.setRole("ADMIN");
            u.setStatus("ACTIVE");
            u.setEnabled(true);
            userRepo.save(u);
        }
    }
}