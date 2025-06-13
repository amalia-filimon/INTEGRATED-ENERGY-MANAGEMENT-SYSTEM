package com.example.device_management_service.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.UUID;

@Entity
public class User implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="id", columnDefinition = "BINARY(16)")
    private UUID id;

    public User() {
    }

    public User(UUID id){
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

}
