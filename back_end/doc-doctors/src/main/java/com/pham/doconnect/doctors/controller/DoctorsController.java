package com.pham.doconnect.doctors.controller;


import com.pham.doconnect.doctors.dto.DoctorsDTO;
import com.pham.doconnect.doctors.service.DoctorsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/doctors")
public class DoctorsController {

    private final DoctorsService doctorsService;
    private static final Logger logger = LoggerFactory.getLogger(DoctorsController.class);

    public DoctorsController(DoctorsService doctorsService) {
        this.doctorsService = doctorsService;
    }

    @GetMapping
    public ResponseEntity<List<DoctorsDTO>> getDoctors() {
        logger.info("Request received in controller to fetch all doctors");
        List<DoctorsDTO> allDoctorsDTO = doctorsService.getAllDoctors();
        logger.info("Returning {} doctors", allDoctorsDTO.size());
        return new ResponseEntity<>(allDoctorsDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorsDTO> getDoctorById(@PathVariable("id") Long id) {
        logger.info("Request received in controller to fetch doctor by id {}", id);
        return new ResponseEntity<>(doctorsService.getDoctorById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DoctorsDTO> createDoctor(@RequestBody DoctorsDTO doctorsDTO) {
        logger.info("Request received in controller to create doctor {}", doctorsDTO);
        DoctorsDTO savedDoctors = doctorsService.saveDoctors(doctorsDTO);
        return new ResponseEntity<>(savedDoctors, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorsDTO> updateDoctor(@PathVariable("id") Long id, @RequestBody DoctorsDTO doctorsDTO) {
        DoctorsDTO editedDoctorsDTO = doctorsService.updateDoctorStatus(id, doctorsDTO);
        logger.info("Request received in controller to update doctor status");
        return new ResponseEntity<>(editedDoctorsDTO, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<DoctorsDTO>> searchDoctors(
            @RequestParam String keyword) {
        logger.info("Request received in controller to search doctors");
        List<DoctorsDTO> listDoctors = doctorsService.searchDoctorsByName(keyword);
        return new ResponseEntity<>(listDoctors, HttpStatus.OK);
    }

    @GetMapping("/search/{specialize}")
    public ResponseEntity<List<DoctorsDTO>> searchDoctorsBySpecialize(@RequestParam String specialize) {
        logger.info("Request received in controller to search doctors by specialize {}", specialize);
        List<DoctorsDTO> listDoctors = doctorsService.searchDoctorBySpecialize(specialize);
        return new ResponseEntity<>(listDoctors, HttpStatus.OK);
    }
}
