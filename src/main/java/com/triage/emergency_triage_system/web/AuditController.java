package com.triage.emergency_triage_system.web;

import com.triage.emergency_triage_system.repo.AuditLogRepo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuditController {

    private final AuditLogRepo auditLogRepo;

    public AuditController(AuditLogRepo auditLogRepo) {
        this.auditLogRepo = auditLogRepo;
    }

    @GetMapping("/audit")
    public String page(Model model) {
        model.addAttribute("logs", auditLogRepo.findAll());
        return "audit";
    }
}