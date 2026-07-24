package com.triage.emergency_triage_system.service;

import com.triage.emergency_triage_system.model.Patient;
import com.triage.emergency_triage_system.model.SmsLog;
import com.triage.emergency_triage_system.repo.SmsLogRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SmsService {

    private final SmsLogRepo smsLogRepo;

    public SmsService(SmsLogRepo smsLogRepo) {
        this.smsLogRepo = smsLogRepo;
    }

    public void sendDemoSms(Patient patient, String message, String type) {
        if (patient == null) {
            return;
        }

        SmsLog sms = new SmsLog();
        sms.setPatientName(patient.getFullName());
        sms.setPhoneNumber(patient.getPhoneNumber());
        sms.setMessage(message);
        sms.setType(type);
        sms.setStatus("SENT DEMO");

        smsLogRepo.save(sms);
    }

    public List<SmsLog> listAll() {
        return smsLogRepo.findAllByOrderBySentAtDesc();
    }
}