package com.pham.doconnect.appointments.repository;

import com.pham.doconnect.appointments.model.Appointments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface AppointmentsRepository extends JpaRepository<Appointments, Long> {
    List<Appointments> findByPatientId(Long patientId);
    List<Appointments> findByDoctorId(Long doctorId);
    List<Appointments> findAppointmentsByAppointmentDate(Date appointmentDate);
}
