package com.pham.doconnect.doctors.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "doctors")
public class Doctors {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String fname;

    @NotNull(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lname;

    @NotNull(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Specialize is required")
    private Specialize specialize;

    @NotNull(message = "Status is required")
    private boolean is_active;

    public Doctors() {
    }

    public Doctors(@NotNull String fname, @NotNull String lname, @NotNull String email, @NotNull Specialize specialize, @NotNull Boolean is_active) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.specialize = specialize;
        this.is_active = is_active;
    }
}
