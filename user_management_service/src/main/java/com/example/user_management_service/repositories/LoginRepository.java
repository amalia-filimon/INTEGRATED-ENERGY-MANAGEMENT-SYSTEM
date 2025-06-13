package com.example.user_management_service.repositories;

import com.example.user_management_service.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LoginRepository extends JpaRepository<User, UUID> {
    Optional<User> findUserByUsername(String username);
}
