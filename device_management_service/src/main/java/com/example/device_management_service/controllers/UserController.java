package com.example.device_management_service.controllers;


import com.example.device_management_service.dtos.DeviceDTO;
import com.example.device_management_service.services.DeviceService;
import com.example.device_management_service.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;



@RestController
@RequestMapping("/api/v1/auth/user")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final DeviceService deviceService;

//    @Autowired
//    public UserController(UserService userService, DeviceService deviceService) {
//        this.userService = userService;
//        this.deviceService = deviceService;
//    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<List<DeviceDTO>> getDevicesById(@PathVariable("id") UUID userId) {
        List<DeviceDTO> dtos = deviceService.findDevicesByUserId(userId);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PostMapping(value = "/{id}")
    public ResponseEntity<UUID> insertUser(@PathVariable("id") UUID userId) {
        //System.out.println("In Controller ID-ul este: " + userId);
        UUID userID = userService.insert(userId);
        return new ResponseEntity<>(userID, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") UUID userId) {
        //inainte sa sterg user-ul trebuie sa sterg toate device-urile lui
        //deviceService.deleteAllUsersDevices(userId);
        deviceService.deleteDevices(userId);
        return ResponseEntity.ok().build();
    }


}
