package com.triage.emergency_triage_system.repo;

import com.triage.emergency_triage_system.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepo extends JpaRepository<AuditLog, Long> {
}