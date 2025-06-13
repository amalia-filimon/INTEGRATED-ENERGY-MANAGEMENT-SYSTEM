package com.example.device_management_service.repositories;

import com.example.device_management_service.entities.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface DeviceRepository extends JpaRepository<Device, UUID> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Device d WHERE d.user_id.id = :userId")
    void deleteByUserId(@Param("userId") UUID userId);


    @Query("SELECT d FROM Device d WHERE d.user_id.id = :userId")
    List<Device> findDeviceByUserId(@Param("userId") UUID userId);


}
