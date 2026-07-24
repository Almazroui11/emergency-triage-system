package com.triage.emergency_triage_system.web;

import com.triage.emergency_triage_system.model.Patient;
import com.triage.emergency_triage_system.repo.PatientRepo;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
public class ExportController {

    private final PatientRepo patientRepo;

    public ExportController(PatientRepo patientRepo) {
        this.patientRepo = patientRepo;
    }

    @GetMapping("/export/patients.csv")
    public ResponseEntity<byte[]> exportPatients() {
        List<Patient> list = patientRepo.findAll();

        StringBuilder sb = new StringBuilder();
        sb.append("id,patientCode,fullName,age,gender,status,category,complaint,arrivalTime\n");
        for (Patient p : list) {
            sb.append(p.getId()).append(",")
              .append(csv(p.getPatientCode())).append(",")
              .append(csv(p.getFullName())).append(",")
              .append(p.getAge()).append(",")
              .append(csv(p.getGender())).append(",")
              .append(csv(p.getStatus())).append(",")
              .append(csv(p.getCategory())).append(",")
              .append(csv(p.getComplaint())).append(",")
              .append(p.getArrivalTime() == null ? "" : p.getArrivalTime())
              .append("\n");
        }

        byte[] bytes = sb.toString().getBytes(StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=patients.csv")
                .contentType(new MediaType("text", "csv", StandardCharsets.UTF_8))
                .body(bytes);
    }

    private String csv(String v) {
        if (v == null) return "";
        String x = v.replace("\"", "\"\"");
        if (x.contains(",") || x.contains("\n") || x.contains("\r")) return "\"" + x + "\"";
        return x;
    }
}