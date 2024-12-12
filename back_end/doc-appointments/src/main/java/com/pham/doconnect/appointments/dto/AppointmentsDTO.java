package com.pham.doconnect.appointments.dto;


import com.pham.doconnect.appointments.model.Appointments;
import com.pham.doconnect.appointments.model.Services;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AppointmentsDTO {

    @NotNull
    private Long id;

    @NotNull
    private Long patientId;

    @NotNull
    private Long doctorId;

    @NotNull
    private Services service;

    @NotNull
    private LocalDateTime appointmentDate;

    public AppointmentsDTO() {}

    public AppointmentsDTO(Appointments appointments) {
        this.id = appointments.getId();
        this.patientId = appointments.getPatientId();
        this.doctorId = appointments.getDoctorId();
        this.service = appointments.getService();
        this.appointmentDate = appointments.getAppointmentDate();
    }

}
