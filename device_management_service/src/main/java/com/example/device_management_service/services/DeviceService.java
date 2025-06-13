package com.example.device_management_service.services;

import com.example.device_management_service.dtos.DeviceDTO;
import com.example.device_management_service.dtos.DeviceDetailsDTO;
import com.example.device_management_service.dtos.builders.DeviceBuilder;
import com.example.device_management_service.entities.Device;
import com.example.device_management_service.entities.User;
import com.example.device_management_service.rabbitMQ.DeviceChangePublisher;
import com.example.device_management_service.repositories.DeviceRepository;
import com.example.device_management_service.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DeviceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceService.class);
    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;

    @Autowired
    private DeviceChangePublisher publisher;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository, UserRepository userRepository) {
        this.deviceRepository = deviceRepository;
        this.userRepository = userRepository;
    }


    public List<DeviceDTO> findDevices() {
//        List<Device> deviceList = deviceRepository.findAll();
//        return deviceList.stream()
//                .map(DeviceBuilder::toDeviceDTO)
//                .collect(Collectors.toList());
        List<Device> deviceList = deviceRepository.findAll();

        return deviceList.stream()
                .map(device -> {
                    DeviceDTO dto = new DeviceDTO();
                    dto.setId(device.getId());
                    dto.setUser_id(device.getUser_id().getId());
                    dto.setDescription(device.getDescription());
                    dto.setAddress(device.getAddress());
                    dto.setMaxHourlyEnergyConsumption(device.getMaxHourlyEnergyConsumption());
                    return dto;
                })
                .collect(Collectors.toList());

    }

//    public List<DeviceDetailsDTO> findDevices() {
//        List<Device> deviceList = deviceRepository.findAll();
//        return deviceList.stream()
//                .map(DeviceBuilder::toDeviceDetailsDTO)
//                .collect(Collectors.toList());
//    }

//    public UUID insert(DeviceDTO deviceDTO) {
//        Device device = DeviceBuilder.toEntity(deviceDTO);
//        device = deviceRepository.save(device);
//        LOGGER.debug("Device with id {} was inserted in db", device.getId());
//        return device.getId();
//    }

    public UUID insert(UUID ownerId, DeviceDetailsDTO deviceDTO) {
        Optional<User> optionalUser = userRepository.findById(ownerId);
        if (!optionalUser.isPresent()) {
            throw new EntityNotFoundException("Owner not found");
        }
        User user = optionalUser.get();
        Device device = new Device(user, deviceDTO.getDescription(), deviceDTO.getAddress(), deviceDTO.getMaxHourlyEnergyConsumption());
        Device deviceReturned = deviceRepository.save(device);
        LOGGER.debug("Device with id {} was inserted in db", device.getId());

        publisher.sendDeviceChange(device.getId(), "inserted", device.getMaxHourlyEnergyConsumption()); //trimit in coada RabbitMQ

        return deviceReturned.getId();
    }

    public void deleteDeviceById(UUID deviceId) {
        publisher.sendDeviceChange(deviceId, "deleted", String.valueOf(-1)); //trimit in coada RabbitMQ
        deviceRepository.deleteById(deviceId);
    }

    public DeviceDTO updateDevice(UUID deviceId, DeviceDTO deviceDTO) {
        Optional<Device> optionalDevice = deviceRepository.findById(deviceId); //gasesc device-ul cu id ul specificat
        if (!optionalDevice.isPresent()) {
            throw new EntityNotFoundException("Device with ID: " + deviceId + "not found");
        }

        Device device = optionalDevice.get(); //salvez device-ul cu toate informatiile

        //actualizez efectiv campurile userului
        device.setDescription(deviceDTO.getDescription());
        device.setAddress(deviceDTO.getAddress());
        device.setMaxHourlyEnergyConsumption(deviceDTO.getMaxHourlyEnergyConsumption());

        //salvez userul actualizat in baza de date
        Device updatedDevice = deviceRepository.save(device);

        publisher.sendDeviceChange(device.getId(), "updated", updatedDevice.getMaxHourlyEnergyConsumption()); //trimit in coada RabbitMQ

        return DeviceBuilder.toDeviceDTO(updatedDevice);
    }

    public void deleteDevices(UUID userId) {
        // Șterge mai întâi toate device-urile asociate cu userId
        deviceRepository.deleteByUserId(userId);

        // După aceea, șterge user-ul
        userRepository.deleteById(userId);
    }

    public List<DeviceDTO> findDevicesByUserId(UUID userId){
        List<Device> deviceList = deviceRepository.findDeviceByUserId(userId);

        return deviceList.stream()
                .map(device -> {
                    DeviceDTO dto = new DeviceDTO();
                    dto.setId(device.getId());
                    dto.setDescription(device.getDescription());
                    dto.setAddress(device.getAddress());
                    dto.setMaxHourlyEnergyConsumption(device.getMaxHourlyEnergyConsumption());
                    return dto;
                })
                .collect(Collectors.toList());

    }

}
