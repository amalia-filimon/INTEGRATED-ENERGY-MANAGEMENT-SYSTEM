package com.example.monitoring_communication_service.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.UUID;

@Entity
public class EnergyConsumption implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name="id", columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)  // Relație Many-to-One cu Device
    @JoinColumn(name = "device_id", nullable = false)  // Coloana device_id este cheia straina
    private Device device_id;

    @Column(name = "measurement_value", nullable = false)
    private double measurement_value;

    @Column(name = "timestamp", nullable = false)
    private long timestamp;

    public EnergyConsumption() {
    }

    public EnergyConsumption(Device device_id, double measurement_value, long timestamp) {
        this.device_id = device_id;
        this.measurement_value = measurement_value;
        this.timestamp = timestamp;
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Device getDevice_id() {
        return device_id;
    }

    public void setDevice_id(Device device_id) {
        this.device_id = device_id;
    }

    public double getMeasurement_value() {
        return measurement_value;
    }

    public void setMeasurement_value(double measurement_value) {
        this.measurement_value = measurement_value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
