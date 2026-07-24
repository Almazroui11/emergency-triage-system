package com.triage.emergency_triage_system.web;

import com.triage.emergency_triage_system.service.SmsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SmsController {

    private final SmsService smsService;

    public SmsController(SmsService smsService) {
        this.smsService = smsService;
    }

    @GetMapping("/sms")
    public String smsPage(Model model) {
        model.addAttribute("smsLogs", smsService.listAll());
        return "sms";
    }
}