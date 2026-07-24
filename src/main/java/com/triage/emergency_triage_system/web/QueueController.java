package com.triage.emergency_triage_system.web;

import com.triage.emergency_triage_system.model.Patient;
import com.triage.emergency_triage_system.service.PatientService;
import com.triage.emergency_triage_system.service.SmsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/queue")
public class QueueController {

    private final PatientService patientService;
    private final SmsService smsService;

    public QueueController(PatientService patientService, SmsService smsService) {
        this.patientService = patientService;
        this.smsService = smsService;
    }

    @GetMapping
    public String queue(Model model) {
        model.addAttribute("patients", patientService.listWaitingPatientsWithEstimate());
        model.addAttribute("archivedPatients", patientService.listArchivedPatients());
        return "queue";
    }

    @PostMapping("/{id}/done")
    public String done(@PathVariable Long id) {
        Patient p = patientService.getById(id);
        p.setStatus("Archived");
        patientService.update(p);

        smsService.sendDemoSms(
                p,
                "Emergency Triage System: Your case has been completed. Thank you.",
                "CASE_COMPLETED"
        );

        return "redirect:/queue";
    }
}