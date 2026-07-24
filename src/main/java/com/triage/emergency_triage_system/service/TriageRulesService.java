package com.triage.emergency_triage_system.service;

import org.springframework.stereotype.Service;

@Service
public class TriageRulesService {

    public String classify(int age, String reason) {
        if (reason == null) return "GREEN";
        String r = reason.toLowerCase();
        if (r.contains("bleeding") || r.contains("unconscious") || r.contains("chest")) {
            return "RED";
        }
        if (r.contains("pain") || r.contains("fracture") || age > 65) {
            return "YELLOW";
        }
        return "GREEN";
    }
}