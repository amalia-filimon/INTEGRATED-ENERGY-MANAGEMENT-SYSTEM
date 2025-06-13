package com.example.device_management_service.controllers;


import com.example.device_management_service.dtos.DeviceDTO;
import com.example.device_management_service.dtos.DeviceDetailsDTO;
import com.example.device_management_service.services.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth/device")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

//    @Autowired
//    public DeviceController(DeviceService deviceService) {
//        this.deviceService = deviceService;
//    }

    @GetMapping()
    public ResponseEntity<List<DeviceDTO>> getDevices() {
        List<DeviceDTO> dtos = deviceService.findDevices();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

//    @GetMapping()
//    public ResponseEntity<List<DeviceDetailsDTO>> getDevices() {
//        List<DeviceDetailsDTO> dtos = deviceService.findDevices();
//        return new ResponseEntity<>(dtos, HttpStatus.OK);
//    }

//    @PostMapping()
//    public ResponseEntity<UUID> insertDevice(@Valid @RequestBody DeviceDTO deviceDTO) {
//        UUID deviceID = deviceService.insert(deviceDTO);
//        return new ResponseEntity<>(deviceID, HttpStatus.CREATED);
//    }

    @PostMapping(value = "/{id}")
    public ResponseEntity<UUID> insertDevice(@PathVariable("id") UUID ownerId, @RequestBody DeviceDetailsDTO deviceDTO) {
        UUID deviceID = deviceService.insert(ownerId, deviceDTO);
        return new ResponseEntity<>(deviceID, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteDevice(@PathVariable("id") UUID deviceId) {
        deviceService.deleteDeviceById(deviceId);
        return ResponseEntity.ok().build();
    }


    @PatchMapping(value = "/{id}")
    public ResponseEntity<DeviceDTO> updateDevice(@PathVariable("id") UUID deviceId, @RequestBody DeviceDTO deviceDTO) {
        DeviceDTO updatedDeviceDTO = deviceService.updateDevice(deviceId, deviceDTO);
        return new ResponseEntity<>(updatedDeviceDTO, HttpStatus.OK);
    }

}
