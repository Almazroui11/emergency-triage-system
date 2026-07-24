package com.triage.emergency_triage_system.repo;

import com.triage.emergency_triage_system.model.SmsLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SmsLogRepo extends JpaRepository<SmsLog, Long> {

    List<SmsLog> findAllByOrderBySentAtDesc();
}