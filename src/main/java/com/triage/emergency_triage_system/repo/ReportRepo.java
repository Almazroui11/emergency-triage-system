package com.triage.emergency_triage_system.repo;

import com.triage.emergency_triage_system.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepo extends JpaRepository<Report, Long> {

    List<Report> findByPatientIdOrderByCreatedAtDesc(Long patientId);

    List<Report> findAllByOrderByCreatedAtDesc();
}