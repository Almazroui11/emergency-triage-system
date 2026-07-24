package com.triage.emergency_triage_system.web;

import com.triage.emergency_triage_system.service.ReportService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DoctorReportsController {

    private final ReportService reportService;

    public DoctorReportsController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/doctor-reports")
    public String doctorReports(Model model) {
        model.addAttribute("reports", reportService.listAll());
        return "doctor-reports";
    }
}