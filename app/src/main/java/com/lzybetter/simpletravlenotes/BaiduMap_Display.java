package com.lzybetter.simpletravlenotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

public class BaiduMap_Display extends Activity {

    private MapView mapView;
    private BaiduMap baiduMap;
    private double[] location;
    private double latitude;
    private double longitude;
    private LocationNow locationNow;
    private boolean isNowLoation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.baidumaplayout);

        isNowLoation = false;
        Intent intent = getIntent();
        isNowLoation = intent.getBooleanExtra("isNowLoation", false);

        mapView=(MapView)findViewById(R.id.map_view);
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        if(isNowLoation) {
            showMyLocation();
        }else {
            latitude = intent.getDoubleExtra("latitude",0);
            longitude = intent.getDoubleExtra("longtitude",0);
            showSavedLocation(latitude, longitude);
        }

    }

    private void showMyLocation(){
        locationNow = new LocationNow();
        location = locationNow.LocationNow(BaiduMap_Display.this);
        latitude = location[0];
        longitude = location[1];
        LatLng myLocation = new LatLng(latitude, longitude);
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(myLocation);
        baiduMap.animateMapStatus(update);
        update = MapStatusUpdateFactory.zoomTo(12f);
        baiduMap.animateMapStatus(update);
        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        locationBuilder.latitude(latitude);
        locationBuilder.longitude(longitude);
        MyLocationData locationData = locationBuilder.build();
        baiduMap.setMyLocationData(locationData);
    }

    private void showSavedLocation(double latitude, double longitude){
        LatLng savedLocation = new LatLng(latitude, longitude);
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(savedLocation);
        baiduMap.animateMapStatus(update);
        update = MapStatusUpdateFactory.zoomTo(12f);
        baiduMap.animateMapStatus(update);
        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        locationBuilder.latitude(latitude);
        locationBuilder.longitude(longitude);
        MyLocationData locationData = locationBuilder.build();
        baiduMap.setMyLocationData(locationData);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        baiduMap.setMyLocationEnabled(false);
        mapView.onDestroy();
        locationNow.removeListener();
    }
}
