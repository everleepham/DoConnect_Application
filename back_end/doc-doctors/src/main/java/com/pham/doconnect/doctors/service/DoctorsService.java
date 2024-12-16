package com.pham.doconnect.doctors.service;


import com.pham.doconnect.doctors.dto.DoctorsDTO;
import com.pham.doconnect.doctors.error.ResourceNotFoundException;
import com.pham.doconnect.doctors.model.Doctors;
import com.pham.doconnect.doctors.repository.DoctorsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorsService {
    public final DoctorsRepository doctorsRepository;
    private static final Logger logger = LoggerFactory.getLogger(DoctorsService.class);

    @Autowired
    public DoctorsService(DoctorsRepository doctorsRepository) {
        this.doctorsRepository = doctorsRepository;
    }

    public List<DoctorsDTO> getAllDoctors() {
        logger.info("Getting all Doctors");
        List<Doctors> allDoctors = doctorsRepository.findAll();
        if (allDoctors.isEmpty()) {
            logger.error("No doctors found");
        } else {
            logger.info("Found {} doctors", allDoctors.size());
        }
        return allDoctors.stream()
                .filter(Doctors::is_active)
                .map(this::toDTO).toList();
    }

    public DoctorsDTO getDoctorById(Long id) {
        logger.info("Getting Patient by ID {}", id);
        Doctors doctor = doctorsRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Doctor with ID {} not found", id);
                    return new ResourceNotFoundException("Doctor not found");
                });
        return toDTO(doctor);
    }

    public DoctorsDTO saveDoctors(DoctorsDTO doctorsDTO) {
        Doctors entity = toEntity(doctorsDTO);
        logger.info("Saving Doctor {}", entity);
        if (doctorsRepository.existsById(entity.getId())) {
            logger.warn("Doctor with ID {} already exists", entity.getId());
            throw new IllegalArgumentException("Doctor with ID " + entity.getId() + " already exists");
        }
        Doctors savedDoctor = doctorsRepository.save(entity);
        logger.info("Saved Doctor {}", savedDoctor);
        return toDTO(savedDoctor);
    }

   public List<DoctorsDTO> searchDoctorsByName(String keyword) {
       if (keyword == null || keyword.trim().isEmpty()) {
           logger.warn("Keyword can not be null");
           throw new IllegalArgumentException("First name or last name must be provided");
       }
        List<Doctors> doctorsFound = doctorsRepository.findByLnameContainingOrFnameContaining(keyword);
       if (doctorsFound.isEmpty()) {
           logger.warn("No doctors with this name found");
       } else {
           logger.info("Found {} doctors with this name", doctorsFound.size());
       }
        return doctorsFound.stream().map(this::toDTO).collect(Collectors.toList());
   }

   public List<DoctorsDTO> searchDoctorBySpecialize(String specialize) {
        if (specialize == null) {
            logger.warn("Specialize can not be null");
            throw new IllegalArgumentException("Specialize can not be null");
        }
        List<Doctors> doctorsFoundBySpe = doctorsRepository.findBySpecialtyContaining(specialize);
        if (doctorsFoundBySpe.isEmpty()) {
            logger.warn("No doctors with this specialize found");
        } else {
            logger.info("Found {} doctors with this specialize ", doctorsFoundBySpe.size());
        }
        return doctorsFoundBySpe.stream().map(this::toDTO).collect(Collectors.toList());
   }

    public DoctorsDTO updateDoctorStatus(Long id, DoctorsDTO doctorsDTO) {
        logger.info("Updating doctor's status with ID {}", id);
        Doctors existingDoc = doctorsRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Doctor with ID {} not found", id);
                    return new ResourceNotFoundException("Doctor not found");
                });
        existingDoc.set_active(doctorsDTO.getIs_active());
        logger.info("Doctor's status with ID {} has been updated successfully", id);
        return toDTO(doctorsRepository.save(existingDoc));
    }

    private DoctorsDTO toDTO(Doctors entity) {
        logger.info("Converting doctor {} to DTO", entity);
        return new DoctorsDTO(entity.getId(), entity.getFname(), entity.getLname(),
                entity.getEmail(), entity.getSpecialize(), entity.is_active());
    }

    private Doctors toEntity(DoctorsDTO dto) {
        logger.info("Converting Doctors to Entity {}", dto);
        Doctors entity = new Doctors();
        entity.setId(dto.getId());
        entity.setLname(dto.getLname());
        entity.setEmail(dto.getEmail());
        entity.setSpecialize(dto.getSpecialize());
        entity.set_active(dto.getIs_active());
        logger.info("Converted Doctors to Entity {}", entity);
        return entity;
    }

}
