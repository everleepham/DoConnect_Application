package com.pham.doconnect.patients.controller;

import com.pham.doconnect.patients.dto.PatientsDTO;
import com.pham.doconnect.patients.service.PatientsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientsController {

    private final PatientsService patientsService;
    private static final Logger logger = LoggerFactory.getLogger(PatientsController.class);

    public PatientsController(PatientsService patientsService) {
        this.patientsService = patientsService;
    }

    @GetMapping
    public ResponseEntity<List<PatientsDTO>> getPatients() {
        logger.info("Request received in controller to fetch all patients");
        List<PatientsDTO> allPatientsDTO = patientsService.getAllPatients();
        logger.info("Returning {} patients in controller", allPatientsDTO.size());
        return new ResponseEntity<>(allPatientsDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientsDTO> getPatientsById(@PathVariable("id") Long id) {
        logger.info("Request received in controller to fetch patients by id {}", id);
        return new ResponseEntity<>(patientsService.getPatientById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PatientsDTO> createPatient(@RequestBody PatientsDTO patientsDTO) {
        PatientsDTO savedPatientsDTO = patientsService.savePatients(patientsDTO);
        logger.info("Request received in controller to create new patient");
        return new ResponseEntity<>(savedPatientsDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientsDTO> editPatient(@PathVariable("id") Long id, @RequestBody PatientsDTO patientsDTO) {
        PatientsDTO editedPatientsDTO = patientsService.updatePatient(id, patientsDTO);
        logger.info("Request received in controller to update patient with id {}", id);
        return new ResponseEntity<>(editedPatientsDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable("id") Long id) {
        patientsService.deletePatient(id);
        logger.info("Request received in controller to delete patient with id {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}



