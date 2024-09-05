package com.example.LCProjectAPI.Controllers;

import com.example.LCProjectAPI.Models.DTO.WeatherDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.LCProjectAPI.Services.WeatherService;

@RestController
public class WeatherController {
   private final WeatherService weatherService;
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }
    @GetMapping("/api/weather")
    public WeatherDTO getWeather(@RequestParam String zipCode) {
        return weatherService.getWeatherByZipCode(zipCode);
    }
}
