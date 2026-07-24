package com.triage.emergency_triage_system.repo;

import com.triage.emergency_triage_system.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PatientRepo extends JpaRepository<Patient, Long> {

    List<Patient> findByStatusOrderByArrivalTimeAsc(String status);

    List<Patient> findByStatusOrderByArrivalTimeDesc(String status);

    long countByStatus(String status);

    long countByCategory(String category);

    @Query("""
            select p from Patient p
            where lower(coalesce(p.fullName,'')) like lower(concat('%', :q, '%'))
               or lower(coalesce(p.patientCode,'')) like lower(concat('%', :q, '%'))
               or lower(coalesce(p.gender,'')) like lower(concat('%', :q, '%'))
               or lower(coalesce(p.status,'')) like lower(concat('%', :q, '%'))
               or lower(coalesce(p.category,'')) like lower(concat('%', :q, '%'))
               or lower(coalesce(p.complaint,'')) like lower(concat('%', :q, '%'))
               or lower(coalesce(p.symptoms,'')) like lower(concat('%', :q, '%'))
            order by p.id desc
            """)
    List<Patient> search(String q);
}