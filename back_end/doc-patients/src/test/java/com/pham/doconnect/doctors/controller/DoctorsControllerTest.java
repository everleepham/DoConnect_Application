package com.pham.doconnect.doctors.controller;


import com.pham.doconnect.doctors.dto.DoctorsDTO;
import com.pham.doconnect.doctors.service.DoctorsService;
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
    @Test
    public void testGetDoctorById() throws Exception {
        when(doctorsService.getDoctorById(1L)).thenReturn(doctorDTO1);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/doctors/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Tim"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("timdoe@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.specialize").value(DENTIST))
                .andExpect(MockMvcResultMatchers.jsonPath("$.is_active").value(true));
    }

    private static final String DOCTOR_JSON = "{\"firstName\":\"Minh\", \"lastName\":\"Pham\", \"email\":\"minhpham2101@gmail.com\", \"specialize\":GENERAL, \"isActive\":false}";


    @Test
    public void testCreateDoctor() throws Exception {
        when(doctorsService.saveDoctors(doctorDTO3)).thenReturn(doctorDTO3);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/doctors")
                    .contentType("application/json")
                    .content(DOCTOR_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Minh"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Pham"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("minhpham2101@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.specialize").value(GENERAL))
                .andExpect(MockMvcResultMatchers.jsonPath("$.is_active").value(false));

    }

    private static final String UPDATE_DOCTOR = "{\"firstName\":\"Minh\", \"lastName\":\"Pham\", \"email\":\"minhpham2101@gmail.com\", \"specialize\":GENERAL, \"isActive\":true}";

    @Test
    public void testUpdateDoctorStatus() throws Exception {
        when(doctorsService.updateDoctorStatus(3L, doctorDTO3)).thenReturn(doctorDTO3);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/doctors/3")
                        .contentType("application/json")
                        .content(UPDATE_DOCTOR))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Minh"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Pham"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("minhpham2101@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.specialize").value(GENERAL))
                .andExpect(MockMvcResultMatchers.jsonPath("$.is_active").value(true));
    }

    @Test
    public void searchDoctorsByName() throws Exception {
        when(doctorsService.searchDoctorsByName("Tim")).thenReturn(List.of(doctorDTO1));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/doctors/search?name=Tim"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value("Tim"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastName").value("Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("timdoe@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].specialize").value(DENTIST))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].is_active").value(true));
    }

    @Test public void searchDoctorsBySpecialize() throws Exception {
        when(doctorsService.searchDoctorBySpecialize("pediatric")).thenReturn(List.of(doctorDTO2));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/doctors/search?specialize=Pediatric"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value("Carla"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastName").value("Smith"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("carla@outlook.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].specialize").value(PEDIATRIC))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].is_active").value(true));
    }

}
