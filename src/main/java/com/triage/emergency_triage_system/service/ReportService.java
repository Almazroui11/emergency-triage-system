package com.triage.emergency_triage_system.service;

import com.triage.emergency_triage_system.model.Patient;
import com.triage.emergency_triage_system.model.Report;
import com.triage.emergency_triage_system.repo.PatientRepo;
import com.triage.emergency_triage_system.repo.ReportRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {

    private final ReportRepo reportRepo;
    private final PatientRepo patientRepo;

    public ReportService(ReportRepo reportRepo, PatientRepo patientRepo) {
        this.reportRepo = reportRepo;
        this.patientRepo = patientRepo;
    }

    public List<Report> listAll() {
        return reportRepo.findAllByOrderByCreatedAtDesc();
    }

    public List<Report> listByPatient(Long patientId) {
        return reportRepo.findByPatientIdOrderByCreatedAtDesc(patientId);
    }

    public void save(Long patientId, String content) {
        Patient p = patientRepo.findById(patientId).orElseThrow();
        Report r = new Report();
        r.setPatient(p);
        r.setContent(content);
        reportRepo.save(r);
    }
}