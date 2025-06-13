package com.example.device_management_service.dtos;

import java.util.UUID;

public class UserDTO {

    private UUID id;

    public UserDTO() {
    }

    public UserDTO(UUID id){
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

}
