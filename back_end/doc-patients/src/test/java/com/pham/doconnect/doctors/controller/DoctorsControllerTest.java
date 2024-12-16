package com.pham.doconnect.doctors.controller;


import com.pham.doconnect.doctors.dto.DoctorsDTO;
import com.pham.doconnect.doctors.model.Doctors;
import com.pham.doconnect.doctors.service.DoctorsService;
import com.pham.doconnect.patients.service.PatientsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static com.pham.doconnect.doctors.model.Specialize.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class DoctorsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private DoctorsService doctorsService;

    @InjectMocks
    private DoctorsController doctorsController;
    private DoctorsDTO doctorDTO1;
    private DoctorsDTO doctorDTO2;
    private DoctorsDTO doctorDTO3;
    private List<DoctorsDTO> doctorsDTOList;

    @BeforeEach
    public void setUp() {
        doctorDTO1 = new DoctorsDTO(1L, "Tim", "Doe", "timdoe@gmail.com", DENTIST, true);
        doctorDTO2 = new DoctorsDTO(2L, "Carla", "Smith", "carla@outlook.com", PEDIATRIC, true);
        doctorDTO3 = new DoctorsDTO(3L, "Minh", "Pham", "minhpham2101@gmail.com", GENERAL, false);

        doctorsDTOList = new ArrayList<>();
        doctorsDTOList.add(doctorDTO1);
        doctorsDTOList.add(doctorDTO2);
        doctorsDTOList.add(doctorDTO3);
    }

    @Test
    public void testGetAllDoctors() throws Exception {
        when(doctorsService.getAllDoctors()).thenReturn(doctorsDTOList);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/doctors"))
                .andExpect(MockMvcResultMatchers.status().isOk())

                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value("Tim"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastName").value("Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("timdoe@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].specialize").value(DENTIST))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].is_active").value(true))

                //returning second patient
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].firstName").value("Carla"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].lastName").value("Smith"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].email").value("carla@outlook.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].specialize").value(PEDIATRIC))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].is_active").value(true));

                //not returning thỉrd doctor because is_active = false
    }

}
