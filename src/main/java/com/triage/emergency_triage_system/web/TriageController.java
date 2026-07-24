package com.triage.emergency_triage_system.web;

import com.triage.emergency_triage_system.model.Patient;
import com.triage.emergency_triage_system.model.TriageRecord;
import com.triage.emergency_triage_system.repo.TriageRepo;
import com.triage.emergency_triage_system.service.PatientService;
import com.triage.emergency_triage_system.service.SmsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/triage")
public class TriageController {

    private final PatientService patientService;
    private final TriageRepo triageRepo;
    private final SmsService smsService;

    public TriageController(PatientService patientService, TriageRepo triageRepo, SmsService smsService) {
        this.patientService = patientService;
        this.triageRepo = triageRepo;
        this.smsService = smsService;
    }

    @GetMapping("/{id}")
    public String form(@PathVariable Long id, Model model) {
        model.addAttribute("patient", patientService.getById(id));
        return "triage";
    }

    @PostMapping("/{id}")
    public String submit(@PathVariable Long id,
                         @RequestParam(required = false) List<String> symptoms,
                         @RequestParam(required = false, defaultValue = "") String notes) {

        Patient patient = patientService.getById(id);

        String symptomsText = "";
        if (symptoms != null && !symptoms.isEmpty()) {
            symptomsText = String.join(", ", symptoms);
        }

        String category = classifyFromSymptoms(patient.getAge(), symptomsText);

        patient.setSymptoms(symptomsText);
        patient.setComplaint(notes);
        patient.setCategory(category);
        patient.setStatus("Waiting");
        patientService.update(patient);

        TriageRecord record = new TriageRecord();
        record.setPatient(patient);
        record.setCategory(category);
        record.setTriageReason(symptomsText);
        record.setTriageNotes(notes);
        record.setTriageTime(LocalDateTime.now());
        triageRepo.save(record);

        String message = switch (category) {
            case "RED" -> "Emergency Triage System: Your case is critical. Please proceed immediately.";
            case "YELLOW" -> "Emergency Triage System: Your case is urgent. Please stay ready for your turn.";
            case "GREEN" -> "Emergency Triage System: Your case is stable. Please wait for your turn.";
            default -> "Emergency Triage System: Your triage has been completed.";
        };

        smsService.sendDemoSms(patient, message, "TRIAGE_" + category);

        return "redirect:/queue";
    }

    private String classifyFromSymptoms(int age, String symptomsText) {
        if (symptomsText == null || symptomsText.isBlank()) {
            return "GREEN";
        }

        String text = symptomsText.toLowerCase();

        if (text.contains("bleeding")
                || text.contains("unconscious")
                || text.contains("chest pain")
                || text.contains("shortness of breath")) {
            return "RED";
        }

        if (text.contains("fracture")
                || text.contains("fever")
                || text.contains("severe pain")
                || age > 65) {
            return "YELLOW";
        }

        return "GREEN";
    }
}