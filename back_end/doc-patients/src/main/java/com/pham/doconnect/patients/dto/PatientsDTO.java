package com.pham.doconnect.patients.dto;


import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

@Getter
@Setter
public class PatientsDTO {
    @NotNull
    private String fname;

    @NotNull
    private String lname;
    private Integer age;
    @NotNull
    private String email;

}
