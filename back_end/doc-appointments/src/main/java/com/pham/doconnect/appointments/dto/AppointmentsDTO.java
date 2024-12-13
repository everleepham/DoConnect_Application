package com.pham.doconnect.appointments.dto;


import com.pham.doconnect.appointments.model.Appointments;
import com.pham.doconnect.appointments.model.Services;
import com.pham.doconnect.doctors.model.Doctors;
import com.pham.doconnect.patients.model.Patients;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

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
        this.patientId = appointments.getPatient().getId();
        this.doctorId = appointments.getDoctor().getId();
        this.service = appointments.getService();
        this.appointmentDate = appointments.getAppointmentDate();
    }

    Date dateConverted(LocalDateTime appointmentDate) {
        return java.util.Date
                .from(appointmentDate.atZone(ZoneId.systemDefault())
                        .toInstant());
    }


}
