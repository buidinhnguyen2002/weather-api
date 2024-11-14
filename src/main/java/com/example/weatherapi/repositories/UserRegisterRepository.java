package com.example.weatherapi.repositories;

import com.example.weatherapi.entities.UserRegister;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRegisterRepository extends JpaRepository<UserRegister, Long> {
    Optional<UserRegister> findByEmail(String email);
    Optional<UserRegister> findByConfirmationToken(String confirmationToken);
}
