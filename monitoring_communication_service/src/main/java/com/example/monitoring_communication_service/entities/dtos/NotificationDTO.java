package com.example.monitoring_communication_service.entities.dtos;

public class NotificationDTO {
    private String deviceId;
    private double suma;
    private double maxHourlyEnergyConsumption;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public double getSuma() {
        return suma;
    }

    public void setSum(double suma) {
        this.suma = suma;
    }

    public double getMaxHourlyEnergyConsumption() {
        return maxHourlyEnergyConsumption;
    }

    public void setMaxHourlyEnergyConsumption(double maxHourlyEnergyConsumption) {
        this.maxHourlyEnergyConsumption = maxHourlyEnergyConsumption;
    }
}
