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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
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
    public List<AppointmentsDTO> getAppointmentByPatient(Long patientId) {
        logger.info("Getting appointment by patient {}", patientId);
        if (patientId == null) {
            logger.warn("Patient not found");
        }
        List<Appointments> appointmentsByPatient = appointmentsRepository.findByPatientId(patientId);
        if (appointmentsByPatient == null || appointmentsByPatient.isEmpty()) {
            logger.warn("No Appointments with this patient found");
        } else {
            logger.info("Found {} appointments of with this patient", appointmentsByPatient.size());
        }
        assert appointmentsByPatient != null;
        return appointmentsByPatient.stream().map(this::toDTO).toList();
    }

    // List apt per doctor
    public List<AppointmentsDTO> getAppointmentByDoctor(Long doctorId) {
        logger.info("Getting appointment by doctor {}", doctorId);
        if (doctorId == null) {
            logger.warn("Doctor not found");
        }
        List<Appointments> appointmentsByDoctor = appointmentsRepository.findByDoctorId(doctorId);
        if (appointmentsByDoctor == null || appointmentsByDoctor.isEmpty()) {
            logger.warn("No Appointments with this doctor found");
        } else {
            logger.info("Found {} appointments of with doctor", appointmentsByDoctor.size());
        }
        assert appointmentsByDoctor != null;
        return appointmentsByDoctor.stream().map(this::toDTO).toList();
    }

    Date convertToDate(LocalDateTime appointmentDate) {
        return java.util.Date
                .from(appointmentDate.atZone(ZoneId.systemDefault())
                        .toInstant());
    }

    // Search apt by date
    public List<AppointmentsDTO> searchApptByDate(LocalDateTime date) {
        logger.info("Searching appointment by date");
        if (date == null) {
            List<Appointments> appointmentsFound = appointmentsRepository.findAll();
        }
        assert date != null;
        List<Appointments> appointmentsFound = appointmentsRepository.findAppointmentsByAppointmentDate(convertToDate(date));
        if (appointmentsFound.isEmpty()) {
            logger.warn("No Appointments found in {}", date);
        } else {
            logger.info("Found {} appointments in this date", appointmentsFound.size());
        }
        assert appointmentsFound != null;
        return appointmentsFound.stream().map(this::toDTO).toList();
    }


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
