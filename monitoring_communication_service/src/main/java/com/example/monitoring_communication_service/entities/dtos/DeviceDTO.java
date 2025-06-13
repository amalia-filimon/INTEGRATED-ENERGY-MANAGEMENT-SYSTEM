package com.example.monitoring_communication_service.entities.dtos;

import jakarta.persistence.Column;

import java.util.UUID;

public class DeviceDTO {
    private UUID id;
    private double maxHourlyEnergyConsumption;

    public DeviceDTO(UUID id, double maxHourlyEnergyConsumption){
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
