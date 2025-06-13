package com.example.monitoring_communication_service.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.UUID;

@Entity
public class Device implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "maxHourlyEnergyConsumption", nullable = false)
    private double maxHourlyEnergyConsumption;

    public Device() {
    }

    public Device(UUID id, double maxHourlyEnergyConsumption){
        this.id = id;
        this.maxHourlyEnergyConsumption = maxHourlyEnergyConsumption;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public double getMaxHourlyEnergyConsumption() {
        return maxHourlyEnergyConsumption;
    }

    public void setMaxHourlyEnergyConsumption(double maxHourlyEnergyConsumption) {
        this.maxHourlyEnergyConsumption = maxHourlyEnergyConsumption;
    }
}
