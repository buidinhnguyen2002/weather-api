package com.example.weatherapi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class HourForecast {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column
    private String time;
    @Column
    private double temp_c;
    @Column
    private double temp_f;
    @Column
    private double windKph;
    @Column
    private double precipMm;
    @Column
    private int humidity;
    @Column
    private int chanceOfRain;
    @Column
    private String textCondition;
    @Column
    private String icon;
}
