package com.triage.emergency_triage_system.web;

import com.triage.emergency_triage_system.model.PendingUser;
import com.triage.emergency_triage_system.repo.PendingUserRepo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final PendingUserRepo pendingRepo;

    public AuthController(PendingUserRepo pendingRepo) {
        this.pendingRepo = pendingRepo;
    }

    @GetMapping({"/", "/home"})
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("pending", new PendingUser());
        return "register";
    }

    @PostMapping("/register")
    public String registerSubmit(@ModelAttribute("pending") PendingUser pending, Model model) {

        if (pending.getEmail() == null || pending.getEmail().isBlank()) {
            model.addAttribute("msg", "Email is required");
            model.addAttribute("pending", pending);
            return "register";
        }

        if (pending.getUsername() == null || pending.getUsername().isBlank()) {
            model.addAttribute("msg", "Username is required");
            model.addAttribute("pending", pending);
            return "register";
        }

        if (pending.getPassword() == null || pending.getPassword().isBlank()) {
            model.addAttribute("msg", "Password is required");
            model.addAttribute("pending", pending);
            return "register";
        }

        if (pendingRepo.existsByUsername(pending.getUsername())) {
            model.addAttribute("msg", "Username already requested");
            model.addAttribute("pending", pending);
            return "register";
        }

        if (pending.getRequestedRole() == null || pending.getRequestedRole().isBlank()) {
            pending.setRequestedRole("NURSE");
        }

        pending.setStatus("PENDING");
        pendingRepo.save(pending);

        return "redirect:/login?requested";
    }

    @GetMapping("/after-login")
    public String afterLogin(Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        boolean isDoctor = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_DOCTOR"));

        boolean isNurse = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_NURSE"));

        if (isAdmin) return "redirect:/users";
        if (isDoctor || isNurse) return "redirect:/dashboard";

        return "redirect:/login?error";
    }
}