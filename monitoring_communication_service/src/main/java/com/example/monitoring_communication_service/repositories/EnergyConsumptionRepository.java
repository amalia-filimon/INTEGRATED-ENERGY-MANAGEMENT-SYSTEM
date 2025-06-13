package com.example.monitoring_communication_service.repositories;

import com.example.monitoring_communication_service.entities.EnergyConsumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface EnergyConsumptionRepository extends JpaRepository<EnergyConsumption, UUID> {
    @Modifying
    @Transactional
    @Query("DELETE FROM EnergyConsumption e WHERE e.device_id.id = :deviceId")
    void deleteByDeviceId(@Param("deviceId") UUID deviceId);

    @Query("SELECT e FROM EnergyConsumption e WHERE e.device_id.id = :deviceId ORDER BY e.timestamp ASC")
    List<EnergyConsumption> getEnergyDataById(@Param("deviceId") UUID deviceId);

}
