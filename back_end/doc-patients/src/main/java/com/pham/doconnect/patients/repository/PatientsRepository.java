package com.pham.doconnect.patients.repository;

import com.pham.doconnect.patients.model.Patients;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientsRepository extends JpaRepository<Patients, Long> {
    boolean existsByEmail(String email);
}
