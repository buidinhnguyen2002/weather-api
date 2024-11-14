package com.example.weatherapi.controllers;

import com.example.weatherapi.entities.UserRegister;
import com.example.weatherapi.repositories.UserRegisterRepository;
import com.example.weatherapi.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@RestController
@CrossOrigin
@RequestMapping("/api/v1/weather")
public class EmailController {
    @Autowired
    private UserRegisterRepository userRegisterRepository;
    @Autowired
    private EmailService emailService;

    @PostMapping("/register-notification")
    public ResponseEntity<Map<String, Object>> register(@RequestParam String email, @RequestParam String location) {
        Optional<UserRegister> existingUser = userRegisterRepository.findByEmail(email);
        if (existingUser.isPresent()) {

            UserRegister user = existingUser.get();
            user.setLocation(location);
            user.setSubscribed(true);
            userRegisterRepository.save(user);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Location updated successfully.");
            return ResponseEntity.ok(response);
        }else{
            emailService.registerNotification(email, location);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Confirmation email sent. Please check your email.");
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> confirmEmail(@RequestParam String token) {
        boolean confirmed = emailService.confirmEmail(token);
        if (confirmed) {
            return ResponseEntity.ok("Email confirmation successful. You have subscribed to receive daily weather forecasts.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Token");
        }
    }
    @PostMapping("/unsubscribe")
    public ResponseEntity<Map<String, Object>> unsubscribe(@RequestParam String email) {
        emailService.unsubscribe(email);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "You have unsubscribed from weather forecasts.");
        return ResponseEntity.ok(response);
    }
}
