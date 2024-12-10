package com.pham.doconnect.doctors.repository;

import com.pham.doconnect.doctors.model.Doctors;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorsRepository extends JpaRepository<Doctors, Long> {
    boolean existsByEmail(String email);

    List<Doctors> findDoctorsByLnameOrFname(String lname, String fname);
}
