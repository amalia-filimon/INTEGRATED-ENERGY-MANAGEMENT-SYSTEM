package com.example.monitoring_communication_service.consumer;
import com.example.monitoring_communication_service.services.DeviceService;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class DeviceChangeListener {

    @Autowired
    private DeviceService deviceService;

    @RabbitListener(queues = "device-change-queue")
    public void receiveMessage(String messageContent) {
        try {

            JSONObject json = new JSONObject(messageContent);

            String action = json.getString("action");
            UUID deviceId = UUID.fromString(json.getString("deviceId"));
            double maxHourlyEnergyConsumption = json.getDouble("maxHourlyEnergyConsumption");

            System.out.println("Action: " + action + ", Device ID: " + deviceId);

             if ("inserted".equals(action)) {
                 deviceService.addDevice(deviceId, maxHourlyEnergyConsumption);
             } else if ("deleted".equals(action)) {
                 deviceService.removeDevice(deviceId);
             }else if ("updated".equals(action)){
                 deviceService.updateDevice(deviceId, maxHourlyEnergyConsumption);
             }
        } catch (Exception e) {
            System.out.println("Error processing message: " + e.getMessage());
        }
    }
}
