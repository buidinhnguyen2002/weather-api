package com.example.weatherapi.repositories;

import com.example.weatherapi.entities.WeatherForecast;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WeatherForecastRepository extends JpaRepository<WeatherForecast, Long> {
    List<WeatherForecast> findByLocationAndDateBetween(String location, LocalDate startDate, LocalDate endDate);
    Optional<WeatherForecast> findByLocationAndDate(String location, LocalDate date);
}
