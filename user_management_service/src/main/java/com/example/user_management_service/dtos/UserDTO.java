package com.example.user_management_service.dtos;

import java.util.Objects;
import java.util.UUID;

public class UserDTO {

    private UUID id;
    private String username;
    private String password;
    private String role;

    public UserDTO() {
    }

    public UserDTO(UUID id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public UserDTO(UUID id, String username, String role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    public UserDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
