package com.example.monitoring_communication_service.consumer;
import com.example.monitoring_communication_service.services.DeviceService;
import com.example.monitoring_communication_service.services.EnergyConsumptionService;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MessageConsumer {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private EnergyConsumptionService energyConsumptionService;

    private Map<UUID, Double> measurementsSum = new HashMap<>(); //suma masuratorilor
    private Map<UUID, Integer> measurementsCount = new HashMap<>(); //un counter pentru cate masuratori sunt citite

    @RabbitListener(queues = "measurements_queue")
    public void receiveMessage(String message) {
        JSONObject json = new JSONObject(message);
        System.out.println("Raw message received: " + message);
        long timestamp = json.getLong("timestamp");
        UUID deviceId = UUID.fromString(json.getString("device_id"));
        double measurementValue = json.getDouble("measurement_value");

//        System.out.println("Received Message: ");
//        System.out.println("Timestamp: " + timestamp);
//        System.out.println("Device ID: " + deviceId);
//        System.out.println("Measurement Value: " + measurementValue);

        measurementsSum.put(deviceId, measurementsSum.getOrDefault(deviceId, 0.0) + measurementValue);
        measurementsCount.put(deviceId, measurementsCount.getOrDefault(deviceId, 0) + 1);

        // verific daca am 6 masuratori pt un anumit dispozitiv
        if (measurementsCount.get(deviceId) == 6) {
            double total = measurementsSum.get(deviceId);
            deviceService.checkEnergyConsumptionAndSendNotification(deviceId, total);

            // Resetez suma si numaratoarea
            measurementsSum.put(deviceId, 0.0);
            measurementsCount.put(deviceId, 0);
        }

        energyConsumptionService.saveEnergyData(deviceId, measurementValue, timestamp);

    }
}
