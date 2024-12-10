package com.pham.doconnect.patients.dto;


import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.*;

@Getter
@Setter
public class PatientsDTO {

    @NotNull
    private Long id;
    private String fname;
    private String lname;
    private Integer age;
    private String email;
    private String password;

    public PatientsDTO() {

    }
    public PatientsDTO(Long id, String fname, String lname, Integer age, String email) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.age = age;
        this.email = email;
    }
}
