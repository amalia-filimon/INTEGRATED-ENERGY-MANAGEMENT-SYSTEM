package com.example.user_management_service.services;

import com.example.user_management_service.dtos.UserDTO;
import com.example.user_management_service.dtos.builders.UserBuilder;
import com.example.user_management_service.entities.User;
import com.example.user_management_service.repositories.LoginRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginService.class);
    private final LoginRepository loginRepository;

    @Autowired
    public LoginService(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    public UserDTO login(UserDTO userDTO) {
        Optional<User> user = loginRepository.findUserByUsername(userDTO.getUsername());
        if(!user.isPresent() || !user.get().getPassword().equals(userDTO.getPassword())){
            return null;
        }

        return UserBuilder.toUserDTOLogin(user.get());
    }

}
