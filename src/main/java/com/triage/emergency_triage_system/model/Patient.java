package com.triage.emergency_triage_system.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String patientCode;
    private String fullName;
    private int age;
    private String gender;
    private String phoneNumber;
    private String status;
    private String category;
    private String complaint;
    private String symptoms;
    private LocalDateTime arrivalTime;

    @Transient
    private String estimatedWaitText;

    public Patient() {
        this.arrivalTime = LocalDateTime.now();
        this.status = "Waiting";
        this.symptoms = "";
    }

    public Long getId() {
        return id;
    }

    public String getPatientCode() {
        return patientCode;
    }

    public void setPatientCode(String patientCode) {
        this.patientCode = patientCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    @Transient
    public String getWaitingTimeText() {
        if (arrivalTime == null) {
            return "-";
        }

        long minutes = Duration.between(arrivalTime, LocalDateTime.now()).toMinutes();

        if (minutes <= 0) {
            return "Just now";
        }

        long hours = minutes / 60;
        long remainingMinutes = minutes % 60;

        if (hours == 0) {
            return minutes + " min";
        }

        if (remainingMinutes == 0) {
            return hours + " h";
        }

        return hours + " h " + remainingMinutes + " min";
    }

    @Transient
    public String getEstimatedWaitText() {
        if (estimatedWaitText != null && !estimatedWaitText.isBlank()) {
            return estimatedWaitText;
        }

        if (category == null) {
            return "-";
        }

        return switch (category.toUpperCase()) {
            case "RED" -> "0 - 5 min";
            case "YELLOW" -> "10 - 20 min";
            case "GREEN" -> "20 - 40 min";
            default -> "-";
        };
    }

    public void setEstimatedWaitText(String estimatedWaitText) {
        this.estimatedWaitText = estimatedWaitText;
    }
}