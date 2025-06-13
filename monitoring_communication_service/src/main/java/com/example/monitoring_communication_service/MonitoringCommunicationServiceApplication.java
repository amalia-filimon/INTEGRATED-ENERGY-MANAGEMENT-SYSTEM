package com.example.monitoring_communication_service;

import com.example.monitoring_communication_service.consumer.MessageConsumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MonitoringCommunicationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MonitoringCommunicationServiceApplication.class, args);
    }

}
