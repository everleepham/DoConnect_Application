package com.pham.doconnect.appointments.controller;


import com.pham.doconnect.appointments.dto.AppointmentsDTO;
import com.pham.doconnect.appointments.model.Appointments;
import com.pham.doconnect.appointments.service.AppointmentsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/appointments")
public class AppointmentsController {
    private final AppointmentsService appointmentsService;
    private static final Logger logger = LoggerFactory.getLogger(AppointmentsController.class);

    public AppointmentsController(AppointmentsService appointmentsService) {
        this.appointmentsService = appointmentsService;
    }

    @GetMapping
    public ResponseEntity<List<AppointmentsDTO>> getAppointments() {
        logger.info("Request received in controller to fetch all appointments");
        List<AppointmentsDTO> allAppointments = appointmentsService.getAllAppointments();
        logger.info("Returning {} appointments", allAppointments.size());
        return new ResponseEntity<>(allAppointments, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentsDTO> getAppointmentById(@PathVariable Long id) {
        logger.info("Request received in controller to fetch appointment by id");
        AppointmentsDTO appointmentsDTO = appointmentsService.getAppointmentById(id);
        logger.info("Returning {} appointment", appointmentsDTO);
        return new ResponseEntity<>(appointmentsDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AppointmentsDTO> createAppointment(@RequestBody AppointmentsDTO appointmentsDTO) {
        logger.info("Request received in controller to create appointment");
        AppointmentsDTO savedAppointments = appointmentsService.createAppointment(appointmentsDTO);
        return new ResponseEntity<>(savedAppointments, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentsDTO> updateAppointment(@PathVariable("id") Long id, @RequestBody AppointmentsDTO appointmentsDTO) {
        AppointmentsDTO updatedAppointments = appointmentsService.updateAppointment(id, appointmentsDTO);
        logger.info("Request received in controller to update appointment date");
        return new ResponseEntity<>(updatedAppointments, HttpStatus.OK);
    }

    @GetMapping("/search/patients/{patient_id}")
    public ResponseEntity<List<AppointmentsDTO>> searchAppointmentsByPatient(@PathVariable  Long patient_id) {
        logger.info("Request received in controller to search appointments by patient");
        List<AppointmentsDTO> listAppointmentsByPatient = appointmentsService.getAppointmentByPatient(patient_id);
        return new ResponseEntity<>(listAppointmentsByPatient, HttpStatus.OK);
    }

    @GetMapping("search/doctors/{doctor_id}")
    public ResponseEntity<List<AppointmentsDTO>> searchAppointmentsByDoctor(@PathVariable Long doctor_id) {
        logger.info("Request received in controller to search appointments by doctor");
        List<AppointmentsDTO> listAppointmentsByDoctor = appointmentsService.getAppointmentByDoctor(doctor_id);
        return new ResponseEntity<>(listAppointmentsByDoctor, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<AppointmentsDTO>> searchAppointmentsByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        logger.info("Request received in controller to search appointments by date");
        List<AppointmentsDTO> listAppointmentsByDate = appointmentsService.getAppointmentsByDate(date);
        return new ResponseEntity<>(listAppointmentsByDate, HttpStatus.OK);
    }


}
