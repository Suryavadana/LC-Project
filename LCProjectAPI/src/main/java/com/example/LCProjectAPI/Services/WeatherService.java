package com.example.LCProjectAPI.Services;

import com.example.LCProjectAPI.Models.DTO.WeatherDTO;
import com.example.LCProjectAPI.Models.WeatherApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

public class WeatherService {
    private final RestTemplate restTemplate;

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.url}")
    private String apiUrl;

    public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public WeatherDTO getWeatherByZipCode(String zipCode) {
        String url = String.format("%s?zip=%s,us&cnt=1&units=imperial&appid=%s", apiUrl, zipCode, apiKey);
        WeatherApiResponse apiResponse = restTemplate.getForObject(url, WeatherApiResponse.class);
        return mapToWeatherDTO(apiResponse);
    }

    private WeatherDTO mapToWeatherDTO(WeatherApiResponse apiResponse) {
        WeatherDTO weatherDTO = new WeatherDTO();
        weatherDTO.setDestination(apiResponse.getCity().getName());
        weatherDTO.setTemp(apiResponse.getList().get(0).getMain().getTemp());
        return weatherDTO;
    }
}
