package com.example.monitoring_communication_service.controllers;

import com.example.monitoring_communication_service.entities.Device;
import com.example.monitoring_communication_service.entities.EnergyConsumption;
import com.example.monitoring_communication_service.entities.dtos.GraphicDataToSendDTO;
import com.example.monitoring_communication_service.entities.dtos.HourlyEnergyConsumptionDTO;
import com.example.monitoring_communication_service.services.EnergyConsumptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1/auth/calendar")
@RequiredArgsConstructor
public class CalendarController {

    private final EnergyConsumptionService energyConsumptionService;

//    @Autowired
//    public CalendarController(EnergyConsumptionService energyConsumptionService) {
//        this.energyConsumptionService = energyConsumptionService;
//    }

    @PostMapping()
    public ResponseEntity<List<GraphicDataToSendDTO>> historicalEnergyConsumption(@RequestBody HourlyEnergyConsumptionDTO requestData) {
        System.out.println(requestData.getData());
        String responseMessage = "RASPUNS DE LA SERVER: AM PRIMIT DATELE!";
        List<EnergyConsumption> allEnergyConsumption = energyConsumptionService.getAllEnergyData();
        List<EnergyConsumption> filteredConsumption = new ArrayList<>();
        String data = requestData.getData();
        List<Device> deviceList = requestData.getDevices();
        List<UUID> deviceIDList = new ArrayList<>();
        for(Device d : deviceList){
            deviceIDList.add(d.getId());
        }

        LocalDate selectedDate = LocalDate.parse(data);
        int selectedDay = selectedDate.getDayOfMonth();
        int selectedMonth = selectedDate.getMonthValue();
        int selectedYear = selectedDate.getYear();

        for (EnergyConsumption consumption : allEnergyConsumption) {
            long timestamp = consumption.getTimestamp();
            Instant instant = Instant.ofEpochSecond(timestamp);
            ZoneId zoneId = ZoneId.of("Europe/Bucharest");
            ZonedDateTime zonedDateTime = instant.atZone(zoneId);
            int year = zonedDateTime.getYear();
            int month = zonedDateTime.getMonthValue();
            int day = zonedDateTime.getDayOfMonth();

            if(selectedDay == day && selectedMonth == month && selectedYear == year && deviceIDList.contains(consumption.getDevice_id().getId())){
                filteredConsumption.add(consumption);
            }
        }

        Map<Integer, List<EnergyConsumption>> minuteToConsumptionsMap = new HashMap<>();

        for (EnergyConsumption consum : filteredConsumption) {
            long timestamp = consum.getTimestamp();
            Instant instant = Instant.ofEpochSecond(timestamp);
            ZoneId zoneId = ZoneId.of("Europe/Bucharest");
            ZonedDateTime zonedDateTime = instant.atZone(zoneId);
            int minute = zonedDateTime.getMinute();

            if (minuteToConsumptionsMap.containsKey(minute)) {
                List<EnergyConsumption> minuteConsumptions = minuteToConsumptionsMap.get(minute);
                if (minuteConsumptions.size() <= 6) {
                    minuteConsumptions.add(consum);
                }
            } else {
                List<EnergyConsumption> minuteConsumptions = new ArrayList<>();
                minuteConsumptions.add(consum);
                minuteToConsumptionsMap.put(minute, minuteConsumptions);
            }
        }
        List<GraphicDataToSendDTO> listaDeTrimis = new ArrayList<>();
        for (Map.Entry<Integer, List<EnergyConsumption>> entry : minuteToConsumptionsMap.entrySet()) {
            int minut = entry.getKey();
            List<EnergyConsumption> consumptions = entry.getValue();

            int suma = 0;
            for (EnergyConsumption consum : consumptions) {
                suma += consum.getMeasurement_value();
            }
            GraphicDataToSendDTO graphicDataToSendDTO = new GraphicDataToSendDTO(minut, suma);
            listaDeTrimis.add(graphicDataToSendDTO);
            System.out.println("Minutul " + minut + ": Suma măsurătorilor = " + suma);
        }

        return new ResponseEntity<>(listaDeTrimis, HttpStatus.OK);
    }

}
