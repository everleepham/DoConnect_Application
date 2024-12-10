package com.pham.doconnect.doctors.dto;


import com.pham.doconnect.doctors.model.Doctors;
import com.pham.doconnect.doctors.model.Specialize;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.*;

@Getter
@Setter
public class DoctorsDTO {

    @NotNull
    private Long id;

    @NotNull(message = "First name required")
    private String fname;

    @NotNull(message = "First name required")
    private String lname;

    @NotNull(message = "Email required")
    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Specialize is required")
    private Specialize specialize;

    @NotNull(message = "Status is required")
    private Boolean is_active;

    public DoctorsDTO() {
    }

    public DoctorsDTO(Doctors doctor) {
        this.id = doctor.getId();
        this.fname = doctor.getFname();
        this.lname = doctor.getLname();
        this.email = doctor.getEmail();
        this.specialize = doctor.getSpecialize();
        this.is_active = doctor.is_active();
    }

}
