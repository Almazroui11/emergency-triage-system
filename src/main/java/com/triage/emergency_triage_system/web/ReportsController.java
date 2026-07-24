package com.triage.emergency_triage_system.web;

import com.triage.emergency_triage_system.model.Report;
import com.triage.emergency_triage_system.repo.PatientRepo;
import com.triage.emergency_triage_system.repo.ReportRepo;
import com.triage.emergency_triage_system.service.ReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Controller
@RequestMapping("/reports")
public class ReportsController {

    private final PatientRepo patientRepo;
    private final ReportRepo reportRepo;
    private final ReportService reportService;

    public ReportsController(PatientRepo patientRepo, ReportRepo reportRepo, ReportService reportService) {
        this.patientRepo = patientRepo;
        this.reportRepo = reportRepo;
        this.reportService = reportService;
    }

    @GetMapping
    public String page(Model model) {
        model.addAttribute("patients", patientRepo.findAll());
        model.addAttribute("reports", reportService.listAll());
        return "reports";
    }

    @PostMapping("/save")
    public String save(@RequestParam Long patientId, @RequestParam String content) {
        reportService.save(patientId, content);
        return "redirect:/reports";
    }

    @GetMapping("/{id}/content")
    @ResponseBody
    public String getReportContent(@PathVariable Long id) {
        Report report = reportRepo.findById(id).orElse(null);

        if (report == null) {
            return "<div class='report-details-box'><p>Not found</p></div>";
        }

        String patientName = report.getPatient() != null ? HtmlUtils.htmlEscape(report.getPatient().getFullName()) : "N/A";
        String createdAt = report.getCreatedAt() != null ? HtmlUtils.htmlEscape(report.getCreatedAt().toString()) : "-";
        String content = report.getContent() != null ? report.getContent() : "";

        return """
                <div class="report-details-box">
                    <div class="report-details-meta">
                        <p><b>Patient:</b> %s</p>
                        <p><b>ID:</b> %d</p>
                        <p><b>Created:</b> %s</p>
                    </div>
                    <div class="report-details-content">%s</div>
                    <div class="action-row" style="margin-top:16px;">
                        <a href="/reports/%d/download-word" class="btn">Download Word</a>
                        <a href="/reports/%d/download-text" class="btn">Download Text</a>
                        <button type="button" onclick="printReportContent(%d)" class="btn">Print</button>
                    </div>
                </div>
                """.formatted(patientName, report.getId(), createdAt, content, id, id, id);
    }

    @GetMapping("/{id}/download-word")
    public ResponseEntity<byte[]> downloadWord(@PathVariable Long id) {
        Optional<Report> reportOpt = reportRepo.findById(id);

        if (reportOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Report report = reportOpt.get();

        String patientName = report.getPatient() != null ? report.getPatient().getFullName() : "report";
        String createdAt = report.getCreatedAt() != null ? report.getCreatedAt().toString() : "-";
        String content = report.getContent() != null ? report.getContent().replaceAll("<[^>]*>", "") : "";

        String wordContent = """
                Patient Report

                Patient: %s
                Created: %s

                ----------------------------

                %s
                """.formatted(patientName, createdAt, content);

        byte[] bytes = wordContent.getBytes(StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"report-" + id + ".doc\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(bytes);
    }

    @GetMapping("/{id}/download-text")
    public ResponseEntity<byte[]> downloadText(@PathVariable Long id) {
        Optional<Report> reportOpt = reportRepo.findById(id);

        if (reportOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Report report = reportOpt.get();

        String patientName = report.getPatient() != null ? report.getPatient().getFullName() : "report";
        String createdAt = report.getCreatedAt() != null ? report.getCreatedAt().toString() : "-";
        String content = report.getContent() != null ? report.getContent().replaceAll("<[^>]*>", "") : "";

        String textContent = """
                Patient Report

                Patient: %s
                Created: %s

                ----------------------------

                %s
                """.formatted(patientName, createdAt, content);

        byte[] bytes = textContent.getBytes(StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"report-" + id + ".txt\"")
                .contentType(MediaType.TEXT_PLAIN)
                .body(bytes);
    }
}