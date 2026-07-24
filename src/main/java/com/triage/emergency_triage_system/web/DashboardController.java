package com.triage.emergency_triage_system.web;

import com.triage.emergency_triage_system.service.AnalyticsService;
import com.triage.emergency_triage_system.repo.TriageRepo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final AnalyticsService analyticsService;
    private final TriageRepo triageRepo;

    public DashboardController(AnalyticsService analyticsService, TriageRepo triageRepo) {
        this.analyticsService = analyticsService;
        this.triageRepo = triageRepo;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        model.addAttribute("totalPatients", analyticsService.totalPatients());
        model.addAttribute("redCount", analyticsService.redCount());
        model.addAttribute("yellowCount", analyticsService.yellowCount());
        model.addAttribute("greenCount", analyticsService.greenCount());
        model.addAttribute("waitingCount", analyticsService.waitingCount());
        model.addAttribute("archivedCount", analyticsService.archivedCount());
        model.addAttribute("recentTriage", triageRepo.findTop10ByOrderByTriageTimeDesc());

        if (authentication != null) {
            model.addAttribute("currentUsername", authentication.getName());

            String role = authentication.getAuthorities().stream()
                    .findFirst()
                    .map(a -> a.getAuthority().replace("ROLE_", ""))
                    .orElse("USER");

            model.addAttribute("currentRole", role);
        }

        return "dashboard";
    }
}