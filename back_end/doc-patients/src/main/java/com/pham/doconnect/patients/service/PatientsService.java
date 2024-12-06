package com.pham.doconnect.patients.service;


import com.pham.doconnect.patients.error.ResourceNotFoundException;
import com.pham.doconnect.patients.model.Patients;
import com.pham.doconnect.patients.repository.PatientsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientsService {
    private final PatientsRepository patientsRepository;

    @Autowired
    public PatientsService(PatientsRepository patientsRepository) {
        this.patientsRepository = patientsRepository;
    }

    public List<Patients> getAllPatients() {
        return patientsRepository.findAll();
    }

    public Patients getPatientById(Long id) {
        return patientsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
    }

    public Patients savePatient(Patients patient) {
        return patientsRepository.save(patient);
    }

    public void deletePatient(Long id) {
        if (patientsRepository.existsById(id)) {
            patientsRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Patient not found");
        }

    }

}
