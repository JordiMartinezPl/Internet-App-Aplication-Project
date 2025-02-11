package com.labproject.domain;

import jakarta.persistence.*;

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long enumID;

    @Enumerated(EnumType.STRING)
    private EnumRoles name;

    public Role() {
    }

    public Role(EnumRoles name) {
        this.name = name;
    }

    public long getEnumID() {
        return enumID;
    }

    public void setEnumID(long enumID) {
        this.enumID = enumID;
    }

    public EnumRoles getName() {
        return name;
    }

    public void setName(EnumRoles name) {
        this.name = name;
    }
}
