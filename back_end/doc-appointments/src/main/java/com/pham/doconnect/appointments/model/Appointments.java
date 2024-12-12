package com.pham.doconnect.appointments.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Appointments {
    @Id
    @GeneratedValue
    Long id;
    Long clientId;
    Long doctorId;
    Services service;
    LocalDateTime appointmentDate;
}

