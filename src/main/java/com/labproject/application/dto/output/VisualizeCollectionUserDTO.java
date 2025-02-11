package com.labproject.application.dto.output;

import com.labproject.domain.Role;

import java.time.LocalDate;

public class VisualizeCollectionUserDTO {
    private String name;
    private String secondName;
    private String email;
    private LocalDate dateOfBirth;
    private LocalDate dateOfRegistration;
    private Role role;

    public VisualizeCollectionUserDTO() {
    }


    public VisualizeCollectionUserDTO(String name, String secondName, String email, LocalDate dateOfBirth, LocalDate dateOfRegistration, Role role) {
        this.name = name;
        this.secondName = secondName;
        this.email = email;
        this.role = role;
        this.dateOfBirth = dateOfBirth;
        this.dateOfRegistration = dateOfRegistration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDate getDateOfRegistration() {
        return dateOfRegistration;
    }

    public void setDateOfRegistration(LocalDate dateOfRegistration) {
        this.dateOfRegistration = dateOfRegistration;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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
}
