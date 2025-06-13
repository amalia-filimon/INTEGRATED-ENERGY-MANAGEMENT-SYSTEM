package com.example.user_management_service.controllers;


import com.example.user_management_service.dtos.UserDetailsDTO;
import com.example.user_management_service.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth/user")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

//    @Autowired
//    public UserController(UserService userService) {
//        this.userService = userService;
//    }


    @PostMapping()
    public ResponseEntity<UUID> insertUser(@Valid @RequestBody UserDetailsDTO userDTO) {
        userDTO.setRole("USER"); //by default toti userii care sunt inserati vor avea rolul de USER, intrucat poate exista un singur admin
        UUID userID = userService.insert(userDTO);
        return new ResponseEntity<>(userID, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<UserDetailsDTO>> getUsers() {
        List<UserDetailsDTO> dtos = userService.findUsers();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }


    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") UUID userId) {
        userService.deleteUserById(userId);
        return ResponseEntity.ok().build();
    }

    //folosesc patch pt ca vreau sa actualizez doar cateva campuri din user, nu sa inlocuiesc complet userul
    @PatchMapping(value = "/{id}")
    public ResponseEntity<UserDetailsDTO> updateUser(@PathVariable("id") UUID userId, @RequestBody UserDetailsDTO userDetailsDTO) {
        UserDetailsDTO updatedUserDTO = userService.updateUser(userId, userDetailsDTO);
        return new ResponseEntity<>(updatedUserDTO, HttpStatus.OK);
    }


}
