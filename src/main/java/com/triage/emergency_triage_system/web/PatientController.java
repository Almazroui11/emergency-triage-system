package com.triage.emergency_triage_system.web;

import com.triage.emergency_triage_system.model.Patient;
import com.triage.emergency_triage_system.service.PatientService;
import com.triage.emergency_triage_system.service.SmsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;
    private final SmsService smsService;

    public PatientController(PatientService patientService, SmsService smsService) {
        this.patientService = patientService;
        this.smsService = smsService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String q,
                       @RequestParam(required = false) String category,
                       @RequestParam(required = false) String status,
                       @RequestParam(required = false) String gender,
                       @RequestParam(required = false) String code,
                       Model model) {

        model.addAttribute("patients", patientService.listAll(q));
        model.addAttribute("q", q == null ? "" : q);
        model.addAttribute("category", category == null ? "" : category);
        model.addAttribute("status", status == null ? "" : status);
        model.addAttribute("gender", gender == null ? "" : gender);
        model.addAttribute("code", code == null ? "" : code);

        return "patients";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("patient", patientService.prepareNewPatient());
        return "patient_form";
    }

    @PostMapping
    public String create(@ModelAttribute Patient patient) {
        Patient saved = patientService.saveNew(patient);

        smsService.sendDemoSms(
                saved,
                "Emergency Triage System: Your registration is complete. Please wait for triage.",
                "REGISTRATION"
        );

        return "redirect:/patients/" + saved.getId();
    }

    @GetMapping("/{id}")
    public String details(@PathVariable Long id, Model model) {
        model.addAttribute("patient", patientService.getById(id));
        model.addAttribute("triageRecords", patientService.triageHistory(id));
        model.addAttribute("patientReports", patientService.reportHistory(id));
        return "patient_details";
    }
}