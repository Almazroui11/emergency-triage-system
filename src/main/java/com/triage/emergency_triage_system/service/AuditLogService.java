package com.triage.emergency_triage_system.service;

import com.triage.emergency_triage_system.model.AuditLog;
import com.triage.emergency_triage_system.repo.AuditLogRepo;
import org.springframework.stereotype.Service;

@Service
public class AuditLogService {

    private final AuditLogRepo auditLogRepo;

    public AuditLogService(AuditLogRepo auditLogRepo) {
        this.auditLogRepo = auditLogRepo;
    }

    public void log(String username, String action, String entity) {
        AuditLog log = new AuditLog();
        log.setUsername(username);
        log.setAction(action);
        log.setEntityName(entity);
        auditLogRepo.save(log);
    }
}