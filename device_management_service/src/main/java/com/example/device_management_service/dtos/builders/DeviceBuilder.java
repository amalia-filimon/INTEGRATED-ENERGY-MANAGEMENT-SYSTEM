package com.example.device_management_service.dtos.builders;

import com.example.device_management_service.dtos.DeviceDTO;
import com.example.device_management_service.dtos.DeviceDetailsDTO;
import com.example.device_management_service.entities.Device;

public class DeviceBuilder {

    private DeviceBuilder() {
    }

//    public static DeviceDetailsDTO toDeviceDetailsDTO(Device device) {
//        return new DeviceDetailsDTO(device.getUser_id(), device.getDescription(), device.getAddress(), device.getMaxHourlyEnergyConsumption());
//    }

    public static DeviceDTO toDeviceDTO(Device device) {
        return new DeviceDTO(device.getId(), device.getDescription(), device.getAddress(), device.getMaxHourlyEnergyConsumption());
    }

    public static Device toEntity(DeviceDTO deviceDTO) {
        return new Device(deviceDTO.getDescription(),
                deviceDTO.getAddress(),
                deviceDTO.getMaxHourlyEnergyConsumption());
    }

    public static Device toEntityForMapping(DeviceDetailsDTO deviceDTO) {
        return new Device(deviceDTO.getUser_id(),
                deviceDTO.getDescription(),
                deviceDTO.getAddress(),
                deviceDTO.getMaxHourlyEnergyConsumption());
    }
}
