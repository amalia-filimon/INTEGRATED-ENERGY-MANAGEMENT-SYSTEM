package com.example.monitoring_communication_service.repositories;

import com.example.monitoring_communication_service.entities.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface DeviceRepository extends JpaRepository<Device, UUID> {

}
