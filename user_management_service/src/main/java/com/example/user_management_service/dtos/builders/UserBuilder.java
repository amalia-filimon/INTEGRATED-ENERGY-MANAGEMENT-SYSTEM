package com.example.user_management_service.dtos.builders;

import com.example.user_management_service.dtos.UserDTO;
import com.example.user_management_service.dtos.UserDetailsDTO;
import com.example.user_management_service.entities.User;

public class UserBuilder {

    private UserBuilder() {
    }

    public static UserDTO toUserDTOLogin(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getPassword(), user.getRole());
    }

//    public static UserDetailsDTO toUserDetailsDTO(User user) {
//        return new UserDetailsDTO(user.getId(), user.getFullname(), user.getUsername(), user.getAddress());
//    }
    public static UserDetailsDTO toUserDetailsDTO(User user) {
        return new UserDetailsDTO(user.getId(), user.getFullname(), user.getUsername(), user.getPassword(), user.getAddress(), user.getRole());
    }

    public static User toEntity(UserDetailsDTO userDetailsDTO) {
        return new User(userDetailsDTO.getFullname(),
                userDetailsDTO.getUsername(),
                userDetailsDTO.getPassword(),
                userDetailsDTO.getAddress(),
                userDetailsDTO.getRole());
    }
}
