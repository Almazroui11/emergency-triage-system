package com.triage.emergency_triage_system.service;

import com.triage.emergency_triage_system.model.Patient;
import com.triage.emergency_triage_system.model.Report;
import com.triage.emergency_triage_system.model.TriageRecord;
import com.triage.emergency_triage_system.repo.PatientRepo;
import com.triage.emergency_triage_system.repo.ReportRepo;
import com.triage.emergency_triage_system.repo.TriageRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {

    private final PatientRepo patientRepo;
    private final TriageRepo triageRepo;
    private final ReportRepo reportRepo;

    public PatientService(PatientRepo patientRepo, TriageRepo triageRepo, ReportRepo reportRepo) {
        this.patientRepo = patientRepo;
        this.triageRepo = triageRepo;
        this.reportRepo = reportRepo;
    }

    public List<Patient> listAll(String q) {
        if (q == null || q.isBlank()) {
            return patientRepo.findAll();
        }
        return patientRepo.search(q.trim());
    }

    public Patient prepareNewPatient() {
        Patient p = new Patient();
        p.setPatientCode("P-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        return p;
    }

    public Patient saveNew(Patient patient) {
        if (patient.getPatientCode() == null || patient.getPatientCode().isBlank()) {
            patient.setPatientCode("P-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        }
        if (patient.getArrivalTime() == null) {
            patient.setArrivalTime(LocalDateTime.now());
        }
        if (patient.getStatus() == null || patient.getStatus().isBlank()) {
            patient.setStatus("Waiting");
        }
        return patientRepo.save(patient);
    }

    public Patient getById(Long id) {
        return patientRepo.findById(id).orElseThrow();
    }

    public void update(Patient patient) {
        patientRepo.save(patient);
    }

    public List<Patient> listWaitingPatientsWithEstimate() {
        List<Patient> waiting = new ArrayList<>(patientRepo.findByStatusOrderByArrivalTimeAsc("Waiting"));

        int redIndex = 0;
        int yellowIndex = 0;
        int greenIndex = 0;

        for (Patient patient : waiting) {
            String category = patient.getCategory() == null ? "" : patient.getCategory().toUpperCase();

            switch (category) {
                case "RED" -> {
                    patient.setEstimatedWaitText(formatRemaining(redIndex * 5));
                    redIndex++;
                }
                case "YELLOW" -> {
                    patient.setEstimatedWaitText(formatRemaining(yellowIndex * 15));
                    yellowIndex++;
                }
                case "GREEN" -> {
                    patient.setEstimatedWaitText(formatRemaining(greenIndex * 20));
                    greenIndex++;
                }
                default -> patient.setEstimatedWaitText("-");
            }
        }

        return waiting;
    }

    public List<Patient> listArchivedPatients() {
        return patientRepo.findByStatusOrderByArrivalTimeDesc("Archived");
    }

    public List<TriageRecord> triageHistory(Long patientId) {
        return triageRepo.findByPatientIdOrderByTriageTimeDesc(patientId);
    }

    public List<Report> reportHistory(Long patientId) {
        return reportRepo.findByPatientIdOrderByCreatedAtDesc(patientId);
    }

    private String formatRemaining(int minutes) {
        if (minutes <= 0) {
            return "Now";
        }

        int hours = minutes / 60;
        int remaining = minutes % 60;

        if (hours == 0) {
            return minutes + " min";
        }

        if (remaining == 0) {
            return hours + " h";
        }

        return hours + " h " + remaining + " min";
    }
}