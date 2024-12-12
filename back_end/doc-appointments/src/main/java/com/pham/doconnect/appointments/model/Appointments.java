package com.pham.doconnect.appointments.model;

import com.pham.doconnect.doctors.model.Doctors;
import com.pham.doconnect.patients.model.Patients;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@ToString(exclude = {"patient", "doctor"})
@EqualsAndHashCode
@Table(name = "appointments")
public class Appointments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    @NotNull
    private Patients patient;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    @NotNull
    private Doctors doctor;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Service is required")
    private Services service;

    @NotNull(message = "Date is required")
    @Future(message = "Appointment date must be in the future")
    private LocalDateTime appointmentDate;

    public Appointments() {
    }

    public Appointments(Patients patientId, Doctors doctorId, Services service, LocalDateTime appointmentDate) {
        this.patient = patientId;
        this.doctor = doctorId;
        this.service = service;
        this.appointmentDate = appointmentDate;
    }
}

