package com.pham.doconnect.patients.service;


import com.pham.doconnect.patients.dto.PatientsDTO;
import com.pham.doconnect.patients.error.ResourceNotFoundException;
import com.pham.doconnect.patients.model.Patients;
import com.pham.doconnect.patients.repository.PatientsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


@Service
public class PatientsService {
    private final PatientsRepository patientsRepository;
    private static final Logger logger = LoggerFactory.getLogger(PatientsService.class);

    @Autowired
    public PatientsService(PatientsRepository patientsRepository) {
        this.patientsRepository = patientsRepository;
    }

    public List<PatientsDTO> getAllPatients() {
        logger.info("Getting all Patients");
        List<Patients> allPatients =  patientsRepository.findAll();
        if (allPatients.isEmpty()) {
            logger.warn("No Patients found");
        } else {
            logger.info("Found {} Patients", allPatients.size());
        }
        return allPatients.stream().map(this::toDTO).toList();
    }

    public PatientsDTO getPatientById(Long id) {
        logger.info("Getting Patient by ID {}", id);
        Patients patient = patientsRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Patient with ID {} not found", id);
                    return new ResourceNotFoundException("Patient not found or been deleted");
                });
        return toDTO(patient);
    }

    public PatientsDTO savePatients(PatientsDTO patientsDTO) {
        Patients entity = toEntity(patientsDTO);
        logger.info("Saving Patient {}", entity);
        if (patientsRepository.existsByEmail(entity.getEmail())) {
            logger.warn("Patient with email {} already exists", entity.getEmail());
            throw new IllegalArgumentException("Patient already exists");
        }
        Patients saved = patientsRepository.save(entity);
        logger.info("Patient {} has been saved successfully", saved);
        return toDTO(saved);
    }

    public void deletePatient(Long id) {
        logger.info("Deleting Patient with ID {}", id);
        Patients patient = patientsRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Patient with ID {} not found or has been deleted", id);
                    return new ResourceNotFoundException("Patient not found or been deleted");
                });
        patientsRepository.delete(patient);
        logger.info("Patient with ID {} has been deleted successfully", id);
    }

    public PatientsDTO updatePatient(Long id, PatientsDTO dto) {
        logger.info("Updating Patient with ID {}", id);
        Patients existingPatients = patientsRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("No Patient with ID {} is found", id);
                    return new ResourceNotFoundException("Patient not found");
                });
        if (dto.getAge() < 0 && dto.getAge() > 200) {
            logger.error("Age not valid");
            throw new IllegalArgumentException("Please enter a valid age");
        }
        existingPatients.setAge(dto.getAge());
        existingPatients.setFirstName(dto.getFname());
        existingPatients.setLastName(dto.getLname());
        existingPatients.setEmail(dto.getEmail());
        Patients updated = patientsRepository.save(existingPatients);
        logger.info("Patient with ID {} has been updated successfully", id);
        return toDTO(updated);
    }

    private Patients toEntity(PatientsDTO patientsDTO) {
        logger.debug("Converting {} to entity", patientsDTO);
        Patients entity = new Patients();
        entity.setFirstName(patientsDTO.getFname());
        entity.setLastName(patientsDTO.getLname());
        entity.setAge(patientsDTO.getAge());
        entity.setEmail(patientsDTO.getEmail());
        entity.setPassword(patientsDTO.getPassword());
        logger.debug("{} is converted to entity", entity);
        return entity;
    }

    private PatientsDTO toDTO(Patients entity) {
        logger.debug("Converting {} to DTO", entity);
        PatientsDTO dto = new PatientsDTO();
        dto.setFname(entity.getFirstName());
        dto.setLname(entity.getLastName());
        dto.setAge(entity.getAge());
        dto.setEmail(entity.getEmail());
        logger.debug("{} is converted to DTO", dto);
        return dto;
    }

}
