package com.cloudcomputing.project.informationprovider;

import com.google.gson.annotations.SerializedName;

public class AirQualityData {

    private maliciousGas CO;
    private maliciousGas NO2;
    private maliciousGas O3;
    private maliciousGas SO2;

    @SerializedName(value = "PM2.5")
    private maliciousGas PM2;

    private maliciousGas PM10;
    private double overall_aqi;

    public maliciousGas getCO() {
        return CO;
    }

    public void setCO(maliciousGas CO) {
        this.CO = CO;
    }

    public maliciousGas getNO2() {
        return NO2;
    }

    public void setNO2(maliciousGas NO2) {
        this.NO2 = NO2;
    }

    public maliciousGas getO3() {
        return O3;
    }

    public void setO3(maliciousGas o3) {
        O3 = o3;
    }

    public maliciousGas getSO2() {
        return SO2;
    }

    public void setSO2(maliciousGas SO2) {
        this.SO2 = SO2;
    }

    public maliciousGas getPM2() {
        return PM2;
    }

    public void setPM2(maliciousGas PM2) {
        this.PM2 = PM2;
    }

    public maliciousGas getPM10() {
        return PM10;
    }

    public void setPM10(maliciousGas PM10) {
        this.PM10 = PM10;
    }

    public double getOverall_aqi(){
        return overall_aqi;
    }

    public void setOverall_aqi(double overall_aqi){
        this.overall_aqi = overall_aqi;
    }

    @Override
    public String toString() {
        return "Overall Air Quality = " + overall_aqi +
                "\nCarbon monoxide = " + CO.getConcentration() +
                "\nNitrogen dioxide = " + NO2.getConcentration() +
                "\nOzone = " + O3.getConcentration() +
                "\nSulphur dioxide = " + SO2.getConcentration() +
                "\nParticulate matter smaller than 2.5 micron = " + PM2.getConcentration() +
                "\nParticulate matter smaller than 10 micron = " + PM10.getConcentration();
    }

    private class maliciousGas {
        private double concentration;
        private double aqi;

        public double getConcentration() {
            return concentration;
        }

        public void setConcentration(double concentration) {
            this.concentration = concentration;
        }

        public double getAqi() {
            return aqi;
        }

        public void setAqi(double aqi) {
            this.aqi = aqi;
        }

        @Override
        public String toString() {
            return "maliciousGas{" +
                    "concentration=" + concentration +
                    ", aqi=" + aqi +
                    '}';
        }
    }

}
