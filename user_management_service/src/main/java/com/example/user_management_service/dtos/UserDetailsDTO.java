package com.example.user_management_service.dtos;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class UserDetailsDTO {

    private UUID id;
    @NotNull
    private String fullname;
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String address;
    @NotNull
    private String role;

    public UserDetailsDTO() {
    }

    public UserDetailsDTO( String fullname, String username, String password, String address) {
        this.fullname = fullname;
        this.username = username;
        this.password = password;
        this.address = address;
    }

    public UserDetailsDTO(UUID id, String fullname, String username, String password, String role) {
        this.id = id;
        this.fullname = fullname;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public UserDetailsDTO(UUID id, String fullname, String username, String address) {
        this.id = id;
        this.fullname = fullname;
        this.username = username;
        this.address = address;
    }

    public UserDetailsDTO(String fullname, String username, String password, String address, String role) {
        this.fullname = fullname;
        this.username = username;
        this.password = password;
        this.address = address;
        this.role = role;
    }

    public UserDetailsDTO(UUID id, String fullname, String username, String password, String address, String role) {
        this.id = id;
        this.fullname = fullname;
        this.username = username;
        this.password = password;
        this.address = address;
        this.role = role;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
