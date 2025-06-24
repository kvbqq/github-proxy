package com.example.github_proxy.service;

import com.example.github_proxy.client.MedicalClinicClient;
import com.example.github_proxy.model.PatientDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final MedicalClinicClient medicalClinicClient;

    public List<PatientDto> getPatients() {
        return medicalClinicClient.getPatients();
    }

    public PatientDto getPatient(String email) {
        return medicalClinicClient.getPatient(email);
    }
}
