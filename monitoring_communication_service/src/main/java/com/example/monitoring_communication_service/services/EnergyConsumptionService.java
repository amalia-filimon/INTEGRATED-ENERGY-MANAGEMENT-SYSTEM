package com.example.monitoring_communication_service.services;

import com.example.monitoring_communication_service.entities.Device;
import com.example.monitoring_communication_service.entities.EnergyConsumption;
import com.example.monitoring_communication_service.entities.dtos.HourlyEnergyConsumptionDTO;
import com.example.monitoring_communication_service.entities.dtos.MeasurementsDTO;
import com.example.monitoring_communication_service.repositories.DeviceRepository;
import com.example.monitoring_communication_service.repositories.EnergyConsumptionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EnergyConsumptionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnergyConsumptionService.class);
    private final EnergyConsumptionRepository energyConsumptionRepository;
    private final DeviceRepository deviceRepository;

    @Autowired
    public EnergyConsumptionService(EnergyConsumptionRepository repository, DeviceRepository deviceRepository) {
        this.energyConsumptionRepository = repository;
        this.deviceRepository = deviceRepository;
    }

    @Transactional
    public void saveEnergyData(UUID deviceId, double measurementValue, long timestamp) {
        Optional<Device> optionalDevice = deviceRepository.findById(deviceId);
        if(!optionalDevice.isPresent()){
            throw new EntityNotFoundException("Device id not found!");
        }
        Device device = optionalDevice.get();

        EnergyConsumption consumption = new EnergyConsumption(device, measurementValue, timestamp);
        energyConsumptionRepository.save(consumption);
    }

    public List<EnergyConsumption> getAllDataById(UUID deviceId){
        return energyConsumptionRepository.getEnergyDataById(deviceId);
    }

    public List<EnergyConsumption> getAllEnergyData(){
        return energyConsumptionRepository.findAll();
    }

    public List<MeasurementsDTO> getAllEnergyDataDTO(){
        List<EnergyConsumption> lista = energyConsumptionRepository.findAll();
        List<MeasurementsDTO> measurementsDTOS = new ArrayList<>();
        for(EnergyConsumption e : lista){
            MeasurementsDTO m = new MeasurementsDTO(e.getId(), e.getDevice_id().getId(), e.getMeasurement_value(), e.getTimestamp());
            measurementsDTOS.add(m);
        }
        return measurementsDTOS;

    }

}
