package com.lzybetter.simpletravlenotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
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
    private Marker markerTest;
    private BitmapDescriptor bitmapDescriptor;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.baidumaplayout);

        Intent intent = getIntent();
        isNowLoation = intent.getBooleanExtra("isNowLocation", false);

        mapView=(MapView)findViewById(R.id.map_view);
        baiduMap = mapView.getMap();
        if(isNowLoation) {
            showMyLocation();
        }else {
            latitude = intent.getDoubleExtra("latitude",0);
            longitude = intent.getDoubleExtra("longtitude",0);
            position = intent.getIntExtra("position",0);
            showSavedLocation(latitude, longitude);
        }

    }

    private void showMyLocation(){
        baiduMap.setMyLocationEnabled(true);
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
        showMarker(savedLocation);
    }

    private void showMarker(LatLng saveLocation){
            bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.foot);
        MarkerOptions markerOptions = new MarkerOptions().position(saveLocation)
                .icon(bitmapDescriptor).draggable(false).animateType(MarkerOptions.MarkerAnimateType.grow);
        markerTest = (Marker)(baiduMap.addOverlay(markerOptions));
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
        if(baiduMap.isBaiduHeatMapEnabled()){
            baiduMap.setMyLocationEnabled(false);
        }
        mapView.onDestroy();
        super.onDestroy();
        baiduMap.clear();
        markerTest = null;
        if(bitmapDescriptor != null){
            bitmapDescriptor.recycle();
        }
        if(locationNow != null){
            locationNow.removeListener();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(isNowLoation){
            menu.add(Menu.NONE, Menu.FIRST + 1, 1, "保存当前地址");
        }else{
            menu.add(Menu.NONE, Menu.FIRST + 2, 1, "编辑当前地址");
            menu.add(Menu.NONE, Menu.FIRST + 3, 2, "删除当前地址");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case Menu.FIRST + 1:
                Intent intent = new Intent(BaiduMap_Display.this, currentLocationEdit.class);
                intent.putExtra("isCurrentLocation",true);
                startActivity(intent);
                break;
            case Menu.FIRST + 2:
                Intent intent1 = new Intent(BaiduMap_Display.this, currentLocationEdit.class);
                intent1.putExtra("isCurrentLocation",false);
                intent1.putExtra("position",position);
                intent1.putExtra("latitude", latitude);
                intent1.putExtra("longitude",longitude);
                startActivity(intent1);
                break;
            case Menu.FIRST + 3:

        }
        return super.onOptionsItemSelected(item);
    }
}
