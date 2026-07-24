package com.triage.emergency_triage_system.repo;

import com.triage.emergency_triage_system.model.TriageRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TriageRepo extends JpaRepository<TriageRecord, Long> {

    List<TriageRecord> findTop10ByOrderByTriageTimeDesc();

    long countByCategory(String category);

    List<TriageRecord> findByPatientIdOrderByTriageTimeDesc(Long patientId);
}