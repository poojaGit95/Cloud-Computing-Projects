package com.cloudcomputing.project.informationviewer;

import com.cloudcomputing.project.exceptions.InvalidResourceException;
import com.cloudcomputing.project.exceptions.ServerNotActiveException;
import com.cloudcomputing.project.informationprovider.CityInformationProvider;


public class CityInformationViewer {

    private String cityName;

    private String city;

    private CityInformationProvider wrapperClient;

    public CityInformationViewer(String cityName){
        this.wrapperClient = new CityInformationProvider();
        this.city = cityName;
        cityName = cityName.replaceAll(" ", "%20");
        this.cityName = cityName;
    }

    public void getCityInformation(){
        String weatherDetails = getCityWeatherData();
        String airQualityDetails = getCityAirQualityData();
        System.out.println("City entered: "+ city);
        System.out.println("\nWeather details: ");
        System.out.println(weatherDetails);
        System.out.println("\nAir Quality Details: ");
        System.out.println(airQualityDetails);
    }

    private String getCityWeatherData(){
        String weatherData = "";
        try {
            weatherData = wrapperClient.getCityWeatherInformation(cityName);
        } catch (InvalidResourceException e) {
            return e.getMessage().toUpperCase();
        } catch (ServerNotActiveException e) {
            return e.getMessage().toUpperCase();
        }
        return weatherData;
    }

    private String getCityAirQualityData(){
        String airQualityData = "";
        try {
            airQualityData = wrapperClient.getCityAirQualityInformation(cityName);
        } catch (InvalidResourceException e) {
            return e.getMessage().toUpperCase();
        } catch (ServerNotActiveException e) {
            return e.getMessage().toUpperCase();
        }
        return airQualityData;
    }
}
