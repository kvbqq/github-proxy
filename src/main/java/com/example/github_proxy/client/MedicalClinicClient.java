package com.example.github_proxy.client;

import com.example.github_proxy.model.PatientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "medicalClinic", url = "${spring.cloud.openfeign.client.config.medical.clinic.url}")
public interface MedicalClinicClient {
    @GetMapping("/patients")
    List<PatientDto> getPatients();

    @GetMapping("/patients/{email}")
    PatientDto getPatient(@PathVariable String email);
}
