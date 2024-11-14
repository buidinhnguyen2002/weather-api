package com.example.weatherapi.services;
import com.example.weatherapi.entities.HourForecast;
import com.example.weatherapi.entities.WeatherForecast;
import com.example.weatherapi.repositories.WeatherForecastRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class WeatherService {
    @Value("${weatherapi.key}")
    private String apiKey;

    @Autowired
    private WeatherForecastRepository weatherForecastRepository;

    public List<WeatherForecast> getWeatherForecast(String location){
        List<WeatherForecast> forecasts = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();

        String url = "http://api.weatherapi.com/v1/forecast.json?key=" + apiKey + "&q=" + location+"&lang=vi" + "&days=14";
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

        Map<String, Object> locationData = (Map<String, Object>) response.getBody().get("location");
        String city = (String) locationData.get("name");
        Map<String, Object> forecastData = (Map<String, Object>) response.getBody().get("forecast");
        List<Map<String, Object>> forecastDays = (List<Map<String, Object>>) forecastData.get("forecastday");
        for (Map<String, Object> day : forecastDays) {
            WeatherForecast forecast = new WeatherForecast();
            forecast.setLocation(city);
            LocalDate date = LocalDate.parse((String) day.get("date"));
            forecast.setDate(date);
            Map<String, Object> dayData = (Map<String, Object>) day.get("day");
            // Add weather information today
            forecast.setMaxTempC((double) dayData.get("maxtemp_c"));
            forecast.setMaxTempF((double) dayData.get("maxtemp_f"));
            forecast.setMinTempC((double) dayData.get("mintemp_c"));
            forecast.setMinTempF((double) dayData.get("mintemp_f"));
            forecast.setAvgTempC((double) dayData.get("avgtemp_c"));
            forecast.setAvgTempF((double) dayData.get("avgtemp_f"));
            forecast.setMaxWindKph((double) dayData.get("maxwind_kph"));
            forecast.setTotalPrecipMm((double) dayData.get("totalprecip_mm"));
            forecast.setAvgHumidity((int) dayData.get("avghumidity"));
            forecast.setDailyChanceOfRain((int) dayData.get("daily_chance_of_rain"));
            Map<String, Object> dayConditionData = (Map<String, Object>) dayData.get("condition");
            forecast.setTextCondition((String) dayConditionData.get("text"));
            forecast.setIcon((String) dayConditionData.get("icon"));
            List<Map<String, Object>> hourData = (List<Map<String, Object>>) day.get("hour");
            List<HourForecast> hourForecastList = new ArrayList<>();
            for(Map<String, Object> hour : hourData){
                String time = (String) hour.get("time");
                double tempC = (double) hour.get("temp_c");
                double tempF = (double) hour.get("temp_f");
                double windKph = (double) hour.get("wind_kph");
                double precipMm = (double) hour.get("precip_mm");
                int humidity = (int) hour.get("humidity");
                int chanceOfRain = (int) hour.get("chance_of_rain");
                Map<String, Object> condition = (Map<String, Object>) hour.get("condition");
                String textCondition = (String) condition.get("text");
                String icon = (String) condition.get("icon");
                HourForecast hourForecast = new HourForecast();
                hourForecast.setTime(time);
                hourForecast.setTemp_c(tempC);
                hourForecast.setTemp_f(tempF);
                hourForecast.setWindKph(windKph);
                hourForecast.setPrecipMm(precipMm);
                hourForecast.setHumidity(humidity);
                hourForecast.setChanceOfRain(chanceOfRain);
                hourForecast.setTextCondition(textCondition);
                hourForecast.setIcon(icon);
                hourForecastList.add(hourForecast);
            }
            forecast.setHourForecasts(hourForecastList);
            forecasts.add(forecast);
            weatherForecastRepository.save(forecast);
        }
        return forecasts.subList(0,5);
    }

//    public WeatherForecast getWeatherToday(String location){
//        Optional<WeatherForecast> savedForecastToday = getSavedForecastToday(location);
//        if(savedForecastToday.isPresent()){
//            return savedForecastToday.get();
//        }
//        List<WeatherForecast> fetchedForecasts = getWeatherForecast(location);
//        Optional<WeatherForecast> weatherForecastToday = fetchedForecasts.stream().filter(forecast -> forecast.getDate().isEqual(LocalDate.now())).findFirst();
//        return weatherForecastToday.get();
//    }

    public Optional<WeatherForecast> getSavedForecastToday(String location){
        LocalDate today = LocalDate.now();
        return weatherForecastRepository.findByLocationAndDate(location,today);
    }

    public List<WeatherForecast> getSavedForecast(String location) {
        LocalDate today = LocalDate.now();
        LocalDate fourDaysLater = today.plusDays(4);
        return weatherForecastRepository.findByLocationAndDateBetween(location, today, fourDaysLater);
    }

    public List<WeatherForecast> loadMoreForecast(String location, String date){
        LocalDate starDate = LocalDate.parse(date).plusDays(1);
        LocalDate enDate = starDate.plusDays(4);
        return weatherForecastRepository.findByLocationAndDateBetween(location, starDate, enDate);
    }

    public static void main(String[] args) {
        WeatherService weatherService = new WeatherService();
        System.out.println("API kety"+weatherService.apiKey);
        List<WeatherForecast> weatherForecasts = weatherService.getWeatherForecast("Binh Dinh");
        for (WeatherForecast w: weatherForecasts){
            System.out.println(w.getLocation());
            System.out.println("Temp c"+ w.getHourForecasts().get(0).getTemp_c());
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void clearAllData() {
        weatherForecastRepository.deleteAll();
    }
}
