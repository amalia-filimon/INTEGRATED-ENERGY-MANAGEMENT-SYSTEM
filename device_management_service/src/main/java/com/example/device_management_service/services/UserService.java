package com.example.device_management_service.services;

import com.example.device_management_service.entities.User;
import com.example.device_management_service.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UUID insert(UUID userId) {
        //System.out.println("UserService: " + userId);
        User user = new User(userId);
        user.setId(userId);
        user = userRepository.save(user);
        LOGGER.debug("User with id {} was inserted in db", user.getId());
        return user.getId();
    }

//    public void deleteUserById(UUID userId) {
//        userRepository.deleteById(userId);
//    }



}
