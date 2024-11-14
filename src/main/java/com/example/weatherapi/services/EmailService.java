package com.example.weatherapi.services;

import com.example.weatherapi.entities.UserRegister;
import com.example.weatherapi.entities.WeatherForecast;
import com.example.weatherapi.repositories.UserRegisterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmailService {
    @Autowired
    private UserRegisterRepository userRegisterRepository;
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${app.confirmation.url}")
    private String confirmationUrl;
    public void registerNotification(String email, String location){
        String token = UUID.randomUUID().toString();
        UserRegister userRegister = new UserRegister();
        userRegister.setEmail(email);
        userRegister.setLocation(location);
        userRegister.setSubscribed(true);
        userRegister.setConfirmed(false);
        userRegister.setConfirmationToken(token);
        userRegisterRepository.save(userRegister);
        sendEmailConfirm(email, token);
    }

    private void sendEmailConfirm(String email, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Confirm your subscription to receive weather forecast emails");
        message.setText("Please confirm your registration by clicking the following link: "
                + confirmationUrl + "?token=" + token);
        javaMailSender.send(message);
    }

    public boolean confirmEmail(String token){
        Optional<UserRegister> user = userRegisterRepository.findByConfirmationToken(token);
        if(user.isPresent()){
            UserRegister userRegister = user.get();
            userRegister.setConfirmed(true);
            userRegisterRepository.save(userRegister);
            return true;
        }
        return false;
    }

    public void unsubscribe(String email){
        Optional<UserRegister> user = userRegisterRepository.findByEmail(email);
        if(user.isPresent()){
            UserRegister userRegister = user.get();
            userRegister.setSubscribed(false);
            userRegisterRepository.save(userRegister);
        }
    }

    public void sendWeatherEmail(String email, String location,List<WeatherForecast> weatherForecasts){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Today's weather forecast at " + location);
        StringBuilder emailBody = new StringBuilder("Hi, \n");
        emailBody.append("Below is the weather forecast for today and the next 4 days in the area: " + location+"\n\n");
        for(WeatherForecast forecast: weatherForecasts){
            emailBody.append("Ngày: ").append(forecast.getDate()).append("\n")
                    .append("- Temperature:  ").append(forecast.getAvgTempC()).append("°C / ")
                    .append(forecast.getAvgTempF()).append("°F\n")
                    .append("- Maximum wind speed: ").append(forecast.getMaxWindKph()).append(" km/h\n")
                    .append("- Humidity: ").append(forecast.getAvgHumidity()).append("%\n")
                    .append("- Chance of rain:").append(forecast.getDailyChanceOfRain()).append("%\n")
                    .append("- Condition: ").append(forecast.getTextCondition()).append("\n\n");
        }
        message.setText(emailBody.toString());
        javaMailSender.send(message);
    }
}
