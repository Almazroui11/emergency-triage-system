package com.triage.emergency_triage_system.repo;

import com.triage.emergency_triage_system.model.PendingUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PendingUserRepo extends JpaRepository<PendingUser, Long> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<PendingUser> findByStatusOrderByIdDesc(String status);
}