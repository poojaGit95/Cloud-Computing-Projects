package com.cloudcomputing.project.informationprovider;

import java.util.List;

public class WeatherData {

    private List<weatherDetails> weather;
    private temperatureDetails main;
    private windDetails wind;
    private String name;

    public List<weatherDetails> getWeather() {
        return weather;
    }

    public void setWeather(List<weatherDetails> weather) {
        this.weather = weather;
    }

    public temperatureDetails getMain() {
        return main;
    }

    public void setMain(temperatureDetails main) {
        this.main = main;
    }

    public windDetails getWind() {
        return wind;
    }

    public void setWind(windDetails wind) {
        this.wind = wind;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return weather.get(0) +
                "\nTemperature Details: " + main +
                "\nWind Details: " + wind;
    }

    public class weatherDetails {
        private String main;
        private String description;

        public String getMain() {
            return main;
        }

        public void setMain(String main) {
            this.main = main;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return "Weather Type = " + main +
                    "\nWeather Description = " + description;
        }
    }

    public class temperatureDetails {
        private double temp;
        private double feels_like;
        private double temp_min;
        private double temp_max;
        private double pressure;
        private double humidity;

        public double getTemp() {
            temp = 1.8*(temp-273) + 32;
            return temp;
        }

        public void setTemp(double temp) {
            this.temp = temp;
        }

        public double getFeels_like() {
            feels_like = 1.8*(feels_like-273) + 32;
            return feels_like;
        }

        public void setFeels_like(double feels_like) {
            this.feels_like = feels_like;
        }

        public double getTemp_min() {
            temp_min = 1.8*(temp_min-273) + 32;
            return temp_min;
        }

        public void setTemp_min(double temp_min) {
            this.temp_min = temp_min;
        }

        public double getTemp_max() {
            temp_max = 1.8*(temp_max-273) + 32;
            return temp_max;
        }

        public void setTemp_max(double temp_max) {
            this.temp_max = temp_max;
        }

        public double getPressure() {
            return pressure;
        }

        public void setPressure(double pressure) {
            this.pressure = pressure;
        }

        public double getHumidity() {
            return humidity;
        }

        public void setHumidity(double humidity) {
            this.humidity = humidity;
        }

        @Override
        public String toString() {
            return "\nCurrent Temperature = " + String.format("%.2f",getTemp()) + " F" +
                    "\nTemperature feels like = " + String.format("%.2f",getFeels_like()) + " F" +
                    "\nMinimum Temperature = " + String.format("%.2f", getTemp_min()) + " F" +
                    "\nMaximum Temperature = " + String.format("%.2f", getTemp_max()) + " F" +
                    "\nAtmospheric Pressure = " + pressure + " hPa" +
                    "\nHumidity = " + humidity + " %";
        }
    }

    public class windDetails {
        private double speed;
        private double deg;

        public double getSpeed() {
            return speed;
        }

        public void setSpeed(double speed) {
            this.speed = speed;
        }

        public double getDeg() {
            return deg;
        }

        public void setDeg(double deg) {
            this.deg = deg;
        }

        @Override
        public String toString() {
            return "\nWind Speed = " + speed + " meters/sec" +
                    "\nWind direction = " + deg + " degrees";
        }
    }
}
