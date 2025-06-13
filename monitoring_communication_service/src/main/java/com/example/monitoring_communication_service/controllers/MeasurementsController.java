package com.example.monitoring_communication_service.controllers;

import com.example.monitoring_communication_service.entities.dtos.MeasurementsDTO;
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
@RequestMapping("/api/v1/auth/measure")
@RequiredArgsConstructor
public class MeasurementsController {

    private final EnergyConsumptionService energyConsumptionService;

//    @Autowired
//    public MeasurementsController(EnergyConsumptionService energyConsumptionService) {
//        this.energyConsumptionService = energyConsumptionService;
//    }

    @GetMapping()
    public ResponseEntity<List<MeasurementsDTO>> getEnergyMeasurements() {
        List<MeasurementsDTO> dtos = energyConsumptionService.getAllEnergyDataDTO();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }


}
