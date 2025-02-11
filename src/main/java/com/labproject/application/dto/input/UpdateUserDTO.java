package com.labproject.application.dto.input;

import com.labproject.configuration.validation.AtLeastThirteenYears;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class UpdateUserDTO {


    @Size(min = 1, max = 50)
    @Size(min = 1, max = 50)
    @Pattern(regexp = "^[A-Z][a-zA-Z]*$", message = "Name must begin with a capital letter. Also only letters are allowed")
    private String name;
    @Size(min = 1, max = 50)
    @Size(min = 1, max = 50)
    @Pattern(regexp = "^[A-Z][a-zA-Z]*$", message = "SecondName must begin with a capital letter. Also only letters are allowed")
    private String secondName;
    @Email
    private String email;
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")
    private String password;
    @AtLeastThirteenYears()
    private LocalDate dateOfBirth;


    public UpdateUserDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }


}
