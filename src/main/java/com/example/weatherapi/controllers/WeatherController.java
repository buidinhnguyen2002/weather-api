package com.example.weatherapi.controllers;

import com.example.weatherapi.entities.WeatherForecast;
import com.example.weatherapi.repositories.WeatherForecastRepository;
import com.example.weatherapi.services.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/weather")
public class WeatherController {
    @Autowired
    private WeatherForecastRepository weatherRepo;
    @Autowired
    private WeatherService weatherService;

    @GetMapping("/forecast")
    public List<WeatherForecast> getWeatherForecast(@RequestParam String location){
        List<WeatherForecast> savedForecasts = weatherService.getSavedForecast(location);
        if (savedForecasts.isEmpty()) {
            List<WeatherForecast> fetchedForecasts = weatherService.getWeatherForecast(location);
            return fetchedForecasts;
        }
        return savedForecasts;
    }
    @GetMapping("/more-forecast")
    public List<WeatherForecast> getMoreWeatherForecast(@RequestParam String location, @RequestParam String date){
        return weatherService.loadMoreForecast(location, date);
    }

}
