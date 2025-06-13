package com.example.monitoring_communication_service.services;

import com.example.monitoring_communication_service.entities.Device;
import com.example.monitoring_communication_service.entities.dtos.DeviceDTO;
import com.example.monitoring_communication_service.entities.dtos.NotificationDTO;
import com.example.monitoring_communication_service.repositories.DeviceRepository;
import com.example.monitoring_communication_service.repositories.EnergyConsumptionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DeviceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnergyConsumptionService.class);
    private final EnergyConsumptionRepository energyConsumptionRepository;
    private final DeviceRepository deviceRepository;

    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public DeviceService(EnergyConsumptionRepository repository, DeviceRepository deviceRepository, SimpMessagingTemplate simpMessagingTemplate) {
        this.energyConsumptionRepository = repository;
        this.deviceRepository = deviceRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void addDevice(UUID deviceId, double max_hourly_energy_consumption) {
        Device device = new Device(deviceId, max_hourly_energy_consumption);
        deviceRepository.save(device);
    }

    public void removeDevice(UUID deviceId) {
        //sterg intai toate datele asociate acelui dispozitiv din EnergyConsumtion table
        energyConsumptionRepository.deleteByDeviceId(deviceId);
        //sterg apoi device id ul din tabelul Device
        deviceRepository.deleteById(deviceId);
    }

    public void updateDevice(UUID deviceId, double maxHourlyEnergyConsumption) {
        Optional<Device> optionalDevice = deviceRepository.findById(deviceId); //gasesc device-ul cu id ul specificat
        if (!optionalDevice.isPresent()) {
            throw new EntityNotFoundException("Device with ID: " + deviceId + "not found");
        }
        Device device = optionalDevice.get(); //salvez device-ul cu toate informatiile
        device.setMaxHourlyEnergyConsumption(maxHourlyEnergyConsumption);
        deviceRepository.save(device);
    }

    public List<DeviceDTO> getDevices(){
        List<Device> lista = deviceRepository.findAll();
        List<DeviceDTO> deviceDTOS = new ArrayList<>();
        for(Device d : lista){
            DeviceDTO dev = new DeviceDTO(d.getId(), d.getMaxHourlyEnergyConsumption());
            deviceDTOS.add(dev);
        }
        return deviceDTOS;
    }

    public double getMaxHourlyEnergyConsumptionByDeviceId(UUID deviceId){
        Optional<Device> optionalDevice = deviceRepository.findById(deviceId); //gasesc device-ul cu id ul specificat
        if (!optionalDevice.isPresent()) {
            throw new EntityNotFoundException("Device with ID: " + deviceId + "not found");
        }

        Device device = optionalDevice.get();
        return device.getMaxHourlyEnergyConsumption();
    }

    public void checkEnergyConsumptionAndSendNotification(UUID deviceId, double suma){
        System.out.println("IN DEVICE SERVICE, SUMA ESTE: " + suma);

        Optional<Device> optionalDevice = deviceRepository.findById(deviceId); //gasesc device-ul cu id ul specificat
        if (!optionalDevice.isPresent()) {
            throw new EntityNotFoundException("Device with ID: " + deviceId + "not found");
        }

        Device device = optionalDevice.get();
        double maxHourlyEnergyConsumption = device.getMaxHourlyEnergyConsumption();

        if(suma > maxHourlyEnergyConsumption){
            NotificationDTO notification = new NotificationDTO();
            notification.setDeviceId(deviceId.toString());
            notification.setSum(suma);
            notification.setMaxHourlyEnergyConsumption(maxHourlyEnergyConsumption);
            //Convertesc obiectul DTO in JSON
            String notificationJson = null;
            try {
                notificationJson = new ObjectMapper().writeValueAsString(notification);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            simpMessagingTemplate.convertAndSend("/topic/deviceId", notificationJson);
            System.out.println("\n");
            System.out.println("TRIMIT NOTIFICARE CATRE FRONTEND: " + deviceId + "AVEM SUMA DE: " + suma + "SI MAX: " + maxHourlyEnergyConsumption);
            System.out.println("\n");
        }

    }
}
