package com.lzybetter.simpletravlenotes;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

/**
 * Created by lzybetter on 2016/4/12.
 */
public class LocationNow {

    private double latitude, longitude;
    private double[] locationDouble = new double[2];
    private int test;
    private String provider;
    private LocationManager locationManager;


    public double[] LocationNow(Activity activity){

        locationManager = (LocationManager)activity.getSystemService(Context.LOCATION_SERVICE);
        List<String> providerList= locationManager.getProviders(true);
        if(providerList.contains(LocationManager.NETWORK_PROVIDER)){
            provider = LocationManager.NETWORK_PROVIDER;
        }else if(providerList.contains(LocationManager.GPS_PROVIDER)){
            provider = LocationManager.GPS_PROVIDER;
        }else {
            Toast.makeText(activity.getBaseContext(), "未开启定位", Toast.LENGTH_SHORT).show();
            locationDouble[0] = 0;
            locationDouble[1] = 0;
            return locationDouble;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        if(location != null){
            latitude = getLatitude(location);
            longitude = getLongitude(location);
        }
        locationManager.requestLocationUpdates(provider, 5000, 1, locationListener);
        locationDouble[0] = latitude;
        locationDouble[1] = longitude;
        return locationDouble;
    }

    private double getLatitude(Location location){
        return location.getLatitude();
    }

    private double getLongitude(Location location){ return location.getLongitude();}

    public void removeListener(){
        if(locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location){
            latitude = getLatitude(location);
            longitude = getLongitude(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

}
