package com.pham.doconnect.patients.controller;


import com.pham.doconnect.patients.model.Patients;
import com.pham.doconnect.patients.service.PatientsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/patients")
public class PatientsController {
    private final PatientsService patientsService;

    @Autowired
    public PatientsController(PatientsService patientsService1) {
        this.patientsService = patientsService1;
    }

    @GetMapping
    public ResponseEntity<List<Patients>> getPatients() {
        List<Patients> allPatients = patientsService.getAllPatients();
        return new ResponseEntity<>(allPatients, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patients> getPatientsById(Long id) {
        return new ResponseEntity<>(patientsService.getPatientById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Patients> addPatient(@RequestBody Patients patient) {
        Patients createdPatients = patientsService.savePatient(patient);
        return new ResponseEntity<>(createdPatients, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Patients> deletePatient(@PathVariable Long id) {
        patientsService.deletePatient(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
