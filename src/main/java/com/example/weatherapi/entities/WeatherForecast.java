package com.example.weatherapi.entities;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class WeatherForecast {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column
    private String location;
    @Column
    private LocalDate date;
    @Column
    private double maxTempC;
    @Column
    private double minTempC;
    @Column
    private double avgTempC;
    @Column
    private double maxTempF;
    @Column
    private double minTempF;
    @Column
    private double avgTempF;
    @Column
    private double maxWindKph;
    @Column
    private double totalPrecipMm;
    @Column
    private int avgHumidity;
    @Column
    private int dailyChanceOfRain;
    @Column
    private String textCondition;
    @Column
    private String icon;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "fk_weather_id", referencedColumnName = "id")
    private List<HourForecast> hourForecasts;
}
