package com.example.user_management_service.services;

import com.example.user_management_service.dtos.UserDetailsDTO;
import com.example.user_management_service.dtos.builders.UserBuilder;
import com.example.user_management_service.entities.User;
import com.example.user_management_service.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UUID insert(UserDetailsDTO userDTO) {
        User user = UserBuilder.toEntity(userDTO);
        user = userRepository.save(user);
        LOGGER.debug("User with id {} was inserted in db", user.getId());
        return user.getId();
    }

    public List<UserDetailsDTO> findUsers() {
        List<User> usersList = userRepository.findAll();
        return usersList.stream()
                .map(UserBuilder::toUserDetailsDTO)
                .collect(Collectors.toList());
    }

    public void deleteUserById(UUID userId) {
        userRepository.deleteById(userId);
    }

    public UserDetailsDTO updateUser(UUID userId, UserDetailsDTO userDetailsDTO) {
        Optional<User> optionalUser = userRepository.findById(userId); //gasesc userul cu id ul specificat
        if (!optionalUser.isPresent()) {
            throw new EntityNotFoundException("User with ID: " + userId + "not found");
        }

        User user = optionalUser.get(); //salvez userul cu toate informatiile

        //actualizez efectiv campurile userului
        user.setFullname(userDetailsDTO.getFullname());
        user.setUsername(userDetailsDTO.getUsername());
        user.setAddress(userDetailsDTO.getAddress());

        //salvez userul actualizat in baza de date
        User updatedUser = userRepository.save(user);


        return UserBuilder.toUserDetailsDTO(updatedUser);
    }


    public UUID findUserByUsernameForDeviceOwner(String username) {
        List<User> usersList = userRepository.findAll(); //pun intr-o lista toti userii
        UUID idUser = null;
        for(User user : usersList){
            if(user.getUsername().equals(username)){
                idUser = user.getId();
            }
        }

        return idUser;
    }
}
