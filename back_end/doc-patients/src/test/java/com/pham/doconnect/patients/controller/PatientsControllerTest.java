package com.pham.doconnect.patients.controller;

import com.pham.doconnect.patients.dto.PatientsDTO;
import com.pham.doconnect.patients.service.PatientsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
public class PatientsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private PatientsService patientsService;

    @InjectMocks
    private PatientsController patientsController;
    private PatientsDTO patientDTO1;
    private PatientsDTO patientDTO2;
    private PatientsDTO patientDTO3;
    private List<PatientsDTO> patientsDTOList;


    @BeforeEach
    public void setUp() {
        patientDTO1 = new PatientsDTO(1L, "John", "Doe", 24, "johndoe@gmail.com");
        patientDTO2 = new PatientsDTO(2L, "Marie", "Jane", 14, "maryjane@epita.fr");
        patientDTO3 = new PatientsDTO(3L, "Eve", "Pham", 19, "eve@gmail.com");

       patientsDTOList = new ArrayList<>();
       patientsDTOList.add(patientDTO1);
       patientsDTOList.add(patientDTO2);
       patientsDTOList.add(patientDTO3);

    }

    @Test
    public void testGetPatients() throws Exception {
        when(patientsService.getAllPatients()).thenReturn(patientsDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/patients"))
                .andExpect(MockMvcResultMatchers.status().isOk())

                //returning first patient
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastName").value("Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(24))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("johndoe@gmail.com"))

                //returning second patient
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].firstName").value("Marie"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].lastName").value("Jane"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].age").value(14))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].email").value("maryjane@epita.fr"))

                //returning third patient
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].firstName").value("Marie"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].lastName").value("Jane"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].age").value(14))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].email").value("maryjane@epita.fr"));

    }




}
