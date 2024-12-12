package com.pham.doconnect.appointments.repository;

import com.pham.doconnect.appointments.model.Appointments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentsRepository extends JpaRepository<Appointments, Long> {
}
