package com.pham.doconnect.appointments.service;


import com.pham.doconnect.appointments.dto.AppointmentsDTO;
import com.pham.doconnect.appointments.model.Appointments;
import com.pham.doconnect.appointments.repository.AppointmentsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AppointmentsService {
    private final AppointmentsRepository appointmentsRepository;
    private static final Logger logger = LoggerFactory.getLogger(AppointmentsService.class);

    @Autowired
    public AppointmentsService(AppointmentsRepository appointmentsRepository) {
        this.appointmentsRepository = appointmentsRepository;
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

    private AppointmentsDTO toDTO(Appointments appointments) {
        AppointmentsDTO dto = new AppointmentsDTO();
        dto.setId(appointments.getId());
        dto.setPatientId(appointments.get);
    }

}
