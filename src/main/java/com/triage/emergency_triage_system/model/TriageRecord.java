package com.triage.emergency_triage_system.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class TriageRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Patient patient;

    private String category;
    private String triageReason;
    private String triageNotes;

    private LocalDateTime triageTime;

    public Long getId() { return id; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getTriageReason() { return triageReason; }
    public void setTriageReason(String triageReason) { this.triageReason = triageReason; }

    public String getTriageNotes() { return triageNotes; }
    public void setTriageNotes(String triageNotes) { this.triageNotes = triageNotes; }

    public LocalDateTime getTriageTime() { return triageTime; }
    public void setTriageTime(LocalDateTime triageTime) { this.triageTime = triageTime; }
}