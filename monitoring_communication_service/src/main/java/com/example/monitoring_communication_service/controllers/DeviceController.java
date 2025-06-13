package com.example.monitoring_communication_service.controllers;

import com.example.monitoring_communication_service.entities.dtos.DeviceDTO;
import com.example.monitoring_communication_service.entities.dtos.MeasurementsDTO;
import com.example.monitoring_communication_service.services.DeviceService;
import com.example.monitoring_communication_service.services.EnergyConsumptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1/auth/device")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

//    @Autowired
//    public DeviceController(DeviceService deviceService) {
//        this.deviceService = deviceService;
//    }

    @GetMapping()
    public ResponseEntity<List<DeviceDTO>> getDevices() {
        List<DeviceDTO> dtos = deviceService.getDevices();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }


}
