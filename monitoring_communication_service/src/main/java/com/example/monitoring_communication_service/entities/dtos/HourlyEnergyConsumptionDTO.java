package com.example.monitoring_communication_service.entities.dtos;

import com.example.monitoring_communication_service.entities.Device;

import java.util.List;

public class HourlyEnergyConsumptionDTO {
    private String data;
    private List<Device> devices;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }
}
