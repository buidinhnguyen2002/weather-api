package com.example.weatherapi.services;

import com.example.weatherapi.entities.UserRegister;
import com.example.weatherapi.entities.WeatherForecast;
import com.example.weatherapi.repositories.UserRegisterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WeatherSentEmailScheduler {
    @Autowired
    private UserRegisterRepository userRegisterRepository;

    @Autowired
    private WeatherService weatherService;
    @Autowired
    private EmailService emailService;

    @Scheduled(cron = "0 0 5 * * ?", zone = "Asia/Ho_Chi_Minh")
    public void sendDalyWeatherEmails(){
        List<UserRegister> subscribedUser = userRegisterRepository.findAll()
                .stream()
                .filter(UserRegister::isSubscribed).filter(UserRegister::isConfirmed).collect(Collectors.toList());
        for(UserRegister user: subscribedUser){
            String location = user.getLocation();
            List<WeatherForecast> savedForecasts = weatherService.getSavedForecast(location);
            if (savedForecasts.isEmpty()) {
                List<WeatherForecast> fetchedForecasts = weatherService.getWeatherForecast(location);
                emailService.sendWeatherEmail(user.getEmail(),location, fetchedForecasts);
                continue;
            }
            emailService.sendWeatherEmail(user.getEmail(), location,savedForecasts);
        }
    }
}
