package com.pham.doconnect.appointments.service;


import com.pham.doconnect.appointments.dto.AppointmentsDTO;
import com.pham.doconnect.appointments.model.Appointments;
import com.pham.doconnect.appointments.repository.AppointmentsRepository;
import com.pham.doconnect.doctors.error.ResourceNotFoundException;
import com.pham.doconnect.doctors.model.Doctors;
import com.pham.doconnect.doctors.model.Specialize;
import com.pham.doconnect.patients.model.Patients;
import com.pham.doconnect.patients.repository.PatientsRepository;
import com.pham.doconnect.doctors.repository.DoctorsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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


    // only make appointment with doctor active
    // patient with age > 18 can not make appointment with pediatrician doctor

    public AppointmentsDTO createAppointment(AppointmentsDTO appointmentsDTO) {
        Long doctorId = appointmentsDTO.getDoctorId();
        Doctors doctor = doctorsRepository.findById(doctorId).orElseThrow(
                () -> new ResourceNotFoundException("Doctor not found")
        );
        if (!doctor.is_active()) {
            throw new IllegalStateException("Doctor is not active");
        }

        Long patientId = appointmentsDTO.getPatientId();
        Patients patient = patientsRepository.findById(patientId).orElseThrow(
                () -> new ResourceNotFoundException("Patient not found")
        );
        if (patient.getAge() > 18 && doctor.getSpecialize() == Specialize.PEDIATRIC) {
            throw new IllegalArgumentException("Patient cannot make an appointment with a pediatrician doctor");
        }

        logger.info("Creating appointment for patient ID: {} with doctor ID: {}", patientId, doctorId);

        Appointments appointments = toEntity(appointmentsDTO);
        Appointments savedAppointments = appointmentsRepository.save(appointments);

        logger.info("Saved appointment with ID: {}", savedAppointments.getId());
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


    public List<AppointmentsDTO> getAppointmentByPatient(Long patientId) {
        logger.info("Getting appointment by patient {}", patientId);
        if (patientId == null) {
            logger.warn("Patient not found");
            throw new ResourceNotFoundException("No appointments found for the specified doctor");
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
            throw new ResourceNotFoundException("No appointments found for the specified patient");
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


    public List<AppointmentsDTO> getAppointmentsByDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        logger.info("Filtering appointments for date: {}", date);
        List<Appointments> allAppointments = appointmentsRepository.findAll();
        if (allAppointments.isEmpty()) {
            logger.warn("No appointments found");
            return List.of();
        }
        List<AppointmentsDTO> filteredAppointments = allAppointments.stream()
                .filter(appointment -> appointment.getAppointmentDate().toLocalDate().equals(date))
                .map(this::toDTO)
                .toList();
        logger.info("Found {} appointments for date {}", filteredAppointments.size(), date);
        return filteredAppointments;
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
