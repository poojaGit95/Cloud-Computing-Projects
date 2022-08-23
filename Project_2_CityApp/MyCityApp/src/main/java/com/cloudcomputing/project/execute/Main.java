package com.cloudcomputing.project.execute;

import com.cloudcomputing.project.informationviewer.CityInformationViewer;


public class Main {
    public static void main(String[] args){
        if(args.length==0){
            System.out.println("Enter a valid city name");
            System.exit(0);
        }
        String cityName = args[0];
        for (int i=1; i<args.length; i++) {
            cityName = cityName + " " +args[i];
        }
        CityInformationViewer cityInformationViewer = new CityInformationViewer(cityName);
        cityInformationViewer.getCityInformation();
    }
}
