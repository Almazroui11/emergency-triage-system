package com.triage.emergency_triage_system.web;

import com.triage.emergency_triage_system.repo.PatientRepo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ArchiveController {

    private final PatientRepo patientRepo;

    public ArchiveController(PatientRepo patientRepo) {
        this.patientRepo = patientRepo;
    }

    @GetMapping("/archive")
    public String page(Model model) {
        model.addAttribute("patients", patientRepo.findAll());
        return "archive";
    }
} 