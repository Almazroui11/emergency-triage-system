package com.triage.emergency_triage_system.web;

import com.triage.emergency_triage_system.model.PendingUser;
import com.triage.emergency_triage_system.model.User;
import com.triage.emergency_triage_system.repo.PendingUserRepo;
import com.triage.emergency_triage_system.repo.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UsersController {

    private final UserRepo userRepo;
    private final PendingUserRepo pendingRepo;
    private final PasswordEncoder passwordEncoder;

    public UsersController(UserRepo userRepo, PendingUserRepo pendingRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.pendingRepo = pendingRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userRepo.findAll());
        model.addAttribute("pendingUsers", pendingRepo.findByStatusOrderByIdDesc("PENDING"));
        return "users";
    }

    @PostMapping("/users/pending/{id}/approve")
    public String approve(@PathVariable Long id) {
        PendingUser p = pendingRepo.findById(id).orElse(null);

        if (p == null) {
            return "redirect:/users";
        }

        boolean usernameExists = userRepo.findByUsername(p.getUsername()).isPresent();
        boolean emailExists = userRepo.findByEmail(p.getEmail()).isPresent();

        if (usernameExists || emailExists) {
            p.setStatus("APPROVED");
            pendingRepo.save(p);
            return "redirect:/users";
        }

        User u = new User();
        u.setName(p.getUsername());
        u.setEmail(p.getEmail());
        u.setUsername(p.getUsername());
        u.setPassword(passwordEncoder.encode(p.getPassword()));

        String role = p.getRequestedRole();
        if (role == null || role.isBlank()) {
            role = "NURSE";
        }

        u.setRole(role);
        u.setStatus("ACTIVE");
        u.setEnabled(true);

        userRepo.save(u);

        p.setStatus("APPROVED");
        pendingRepo.save(p);

        return "redirect:/users";
    }

    @PostMapping("/users/pending/{id}/reject")
    public String reject(@PathVariable Long id) {
        PendingUser p = pendingRepo.findById(id).orElse(null);

        if (p == null) {
            return "redirect:/users";
        }

        p.setStatus("REJECTED");
        pendingRepo.save(p);

        return "redirect:/users";
    }
}