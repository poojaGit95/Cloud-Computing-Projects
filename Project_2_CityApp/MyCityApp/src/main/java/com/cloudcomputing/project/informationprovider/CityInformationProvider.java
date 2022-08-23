package com.cloudcomputing.project.informationprovider;

import com.cloudcomputing.project.exceptions.InvalidResourceException;
import com.cloudcomputing.project.exceptions.ServerNotActiveException;
import com.cloudcomputing.project.restclient.RestClient;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Optional;

public class CityInformationProvider {

    private RestClient restClient;

    private Gson gson;

    public CityInformationProvider(){
        this.restClient = new RestClient();
        this.gson = new Gson();
    }

    public String getCityWeatherInformation(String cityName) throws InvalidResourceException, ServerNotActiveException {
        String apiToken = "04ecc8e5f0265bbccdbf737bbe0e584f";
        String url = "http://api.openweathermap.org/data/2.5/weather?q="+cityName+"&appid="+apiToken;
        String weatherData = "";
        try {
             weatherData = restClient.getHttpResponse(url, Optional.empty(), Optional.empty());
             weatherData = convertToWeatherData(weatherData).toString();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }catch (InvalidResourceException e) {
            throw e;
        }catch (ServerNotActiveException e) {
            throw e;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return weatherData;
    }

    public String getCityAirQualityInformation(String cityName) throws InvalidResourceException, ServerNotActiveException {
        String url = "https://api.api-ninjas.com/v1/airquality?city="+cityName;
        String apiKey = "X-Api-Key";
        String apiToken = "2QOHQDSu1Z3ed9xVv21+Xg==XqCZZNJxSzO89WaU";
        String airQualityData = "";
        try {
            airQualityData = restClient.getHttpResponse(url, Optional.of(apiKey), Optional.of(apiToken));
            airQualityData = convertToAirQualityData(airQualityData).toString();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvalidResourceException e) {
            throw e;
        }catch (ServerNotActiveException e) {
            throw e;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return airQualityData;
    }

    private AirQualityData convertToAirQualityData(String airQualityData){
        AirQualityData aqData = gson.fromJson(airQualityData, AirQualityData.class);
        return aqData;
    }

    private WeatherData convertToWeatherData(String weatherData){
        WeatherData wData = gson.fromJson(weatherData, WeatherData.class);
        return wData;
    }
}
