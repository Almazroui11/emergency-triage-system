package com.triage.emergency_triage_system.web;

import com.triage.emergency_triage_system.service.AnalyticsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/analytics")
    public String page(Model model) {
        model.addAttribute("totalPatients", analyticsService.totalPatients());
        model.addAttribute("redCount", analyticsService.redCount());
        model.addAttribute("yellowCount", analyticsService.yellowCount());
        model.addAttribute("greenCount", analyticsService.greenCount());
        return "analytics";
    }
}