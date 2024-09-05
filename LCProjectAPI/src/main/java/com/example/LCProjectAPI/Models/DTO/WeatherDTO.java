package com.example.LCProjectAPI.Models.DTO;

public class WeatherDTO {

    private String destination;
    private double temp;

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }
}
