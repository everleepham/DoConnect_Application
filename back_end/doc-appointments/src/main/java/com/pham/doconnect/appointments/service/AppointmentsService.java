package com.pham.doconnect.appointments.service;


import com.pham.doconnect.appointments.dto.AppointmentsDTO;
import com.pham.doconnect.appointments.model.Appointments;
import com.pham.doconnect.appointments.repository.AppointmentsRepository;
import com.pham.doconnect.doctors.error.ResourceNotFoundException;
import com.pham.doconnect.doctors.model.Doctors;
import com.pham.doconnect.patients.model.Patients;
import com.pham.doconnect.patients.repository.PatientsRepository;
import com.pham.doconnect.doctors.repository.DoctorsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentsService {
    private final AppointmentsRepository appointmentsRepository;
    private static final Logger logger = LoggerFactory.getLogger(AppointmentsService.class);
    private final PatientsRepository patientsRepository;
    private final DoctorsRepository doctorsRepository;


    @Autowired
    public AppointmentsService(AppointmentsRepository appointmentsRepository, PatientsRepository patientsRepository, DoctorsRepository doctorsRepository) {
        this.appointmentsRepository = appointmentsRepository;
        this.patientsRepository = patientsRepository;
        this.doctorsRepository = doctorsRepository;
    }

    public List<AppointmentsDTO> getAllAppointments() {
        logger.info("getting all appointments");
        List<Appointments> allAppointments = appointmentsRepository.findAll();
        if (allAppointments.isEmpty()) {
            logger.warn("No Appointments found");
        } else {
            logger.info("Found {} appointments", allAppointments.size());
        } return allAppointments.stream().map(this::toDTO).toList();
    }

    public AppointmentsDTO getAppointmentById(Long id) {
        logger.info("Getting appointment by id {}", id);
        Appointments appointment = appointmentsRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Appointment with ID {} not found", id);
                    return new ResourceNotFoundException("Appointment not found");
                });
        return toDTO(appointment);
    }

    public AppointmentsDTO createAppointment(AppointmentsDTO appointmentsDTO) {
        logger.info("Creating appointment {}", appointmentsDTO);
        Appointments appointments = toEntity(appointmentsDTO);
        if (appointmentsRepository.existsById(appointments.getId())) {
            logger.warn("Appointment with ID {} already exists", appointments.getId());
            throw new IllegalArgumentException("Appointment with ID " + appointments.getId() + " already exists");
        }
        Appointments savedAppointments = appointmentsRepository.save(appointments);
        logger.info("Saved appointment {}", savedAppointments);
        return toDTO(savedAppointments);
    }

    public AppointmentsDTO updateAppointment(Long id, AppointmentsDTO dto) {
        logger.info("Updating appointment {}", dto);
        Appointments existingAppoint = appointmentsRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Appointment with ID {} not found", id);
                    return new ResourceNotFoundException("Appointment not found");
                });
        existingAppoint.setAppointmentDate(dto.getAppointmentDate());
        Appointments updated = appointmentsRepository.save(existingAppoint);
        logger.info("Updated appointment {}", updated);
        return toDTO(updated);
    }

    public void deleteAppointment(Long id) {
        logger.info("Deleting appointment {}", id);
        Appointments appointment = appointmentsRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Appointment with ID {} not found or has been deleted", id);
                    return new ResourceNotFoundException("Appointment not found");
                });
        appointmentsRepository.delete(appointment);
        logger.info("Deleted appointment {}", id);
    }

    // List apt per patient
    // List apt per doctor
    // Search apt by date
    // Unit

    private AppointmentsDTO toDTO(Appointments appointments) {
        logger.info("Converting appointments to DTO");
        AppointmentsDTO dto = new AppointmentsDTO();
        dto.setId(appointments.getId());
        dto.setPatientId(appointments.getPatient().getId());
        dto.setDoctorId(appointments.getDoctor().getId());
        dto.setService(appointments.getService());
        dto.setAppointmentDate(appointments.getAppointmentDate());
        logger.info("Converted appointments to DTO");
        return dto;
    }

    private Appointments toEntity(AppointmentsDTO dto) {
        logger.info("Converting appointments DTO to Entity");
        Appointments entity = new Appointments();
        Patients patient = patientsRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));
        Doctors doctor = doctorsRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));
        entity.setId(dto.getId());
        entity.setPatient(patient);
        entity.setDoctor(doctor);
        entity.setService(dto.getService());
        entity.setAppointmentDate(dto.getAppointmentDate());
        logger.info("Converted appointments DTO to Entity");
        return entity;
    }

}
