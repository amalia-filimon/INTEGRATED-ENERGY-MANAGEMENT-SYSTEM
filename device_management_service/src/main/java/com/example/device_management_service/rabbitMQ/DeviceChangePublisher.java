package com.example.device_management_service.rabbitMQ;

import org.json.JSONObject;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.amqp.core.TopicExchange;

@Service
public class DeviceChangePublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private TopicExchange exchange;

    public void sendDeviceChange(UUID deviceId, String action, String maxHourlyEnergyConsumption) {
        //folosesc HashMap(nu JSON) pt ca se serializeaza de 2 ori si se trimite obiectul JSON ca string
        Map<String, Object> message = new HashMap<>();
        message.put("action", action);
        message.put("deviceId", deviceId.toString());
        message.put("maxHourlyEnergyConsumption", maxHourlyEnergyConsumption);

        System.out.println("Sending message: " + message);

        rabbitTemplate.convertAndSend(exchange.getName(), "device.change", message);
    }
}
