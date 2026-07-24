package com.triage.emergency_triage_system.service;

import com.triage.emergency_triage_system.repo.PatientRepo;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsService {

    private final PatientRepo patientRepo;

    public AnalyticsService(PatientRepo patientRepo) {
        this.patientRepo = patientRepo;
    }

    public long totalPatients() {
        return patientRepo.count();
    }

    public long redCount() {
        return patientRepo.countByCategory("RED");
    }

    public long yellowCount() {
        return patientRepo.countByCategory("YELLOW");
    }

    public long greenCount() {
        return patientRepo.countByCategory("GREEN");
    }

    public long waitingCount() {
        return patientRepo.countByStatus("Waiting");
    }

    public long archivedCount() {
        return patientRepo.countByStatus("Archived");
    }
}