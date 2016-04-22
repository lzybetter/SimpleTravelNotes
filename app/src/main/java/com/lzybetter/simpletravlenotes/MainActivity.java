package com.lzybetter.simpletravlenotes;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class MainActivity extends Activity {

    private final static int SHOW_LOCATION_NAME = 1;

    private Button button, baiduMapButton, saveLocationButton;
    private Button  saveButton,readButton,pictureButton;
    private TextView locationTextView;
    private ImageView imageView;
    private double latitude, longtitude;
    private LocationNow locationNow;
    private EditText inputLatitude, inputLongitude, inputSaveLocationName, inputLocationDescribe;
    private SaveLocationDatabaseHelper saveLocationDatabaseHelper;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        saveLocationDatabaseHelper = new SaveLocationDatabaseHelper(this, "SaveLocation.db", null, 5);
        locationTextView = (TextView)findViewById(R.id.textView);
        button = (Button)findViewById(R.id.button);
        baiduMapButton = (Button)findViewById(R.id.baiduMapButton);
        saveLocationButton = (Button)findViewById(R.id.savedLocationButton);
        saveButton = (Button)findViewById(R.id.saveButton);
        readButton = (Button)findViewById(R.id.readButton);
        pictureButton = (Button)findViewById(R.id.pictureButton);
        inputLatitude = (EditText)findViewById(R.id.inputLatitude);
        inputLongitude = (EditText)findViewById(R.id.inputLongitude);
        inputSaveLocationName = (EditText)findViewById(R.id.input_locationName) ;
        inputLocationDescribe = (EditText)findViewById(R.id.input_locationDescribe);
        imageView = (ImageView)findViewById(R.id.imageView);

        ButtonListener buttonListener = new ButtonListener();
        button.setOnClickListener(buttonListener);
        baiduMapButton.setOnClickListener(buttonListener);
        saveLocationButton.setOnClickListener(buttonListener);
        saveButton.setOnClickListener(buttonListener);
        readButton.setOnClickListener(buttonListener);
        pictureButton.setOnClickListener(buttonListener);
    }

    class ButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.button:
                    locationNow = new LocationNow();
                    double[] location = locationNow.LocationNow(MainActivity.this);
                    latitude = location[0];
                    longtitude = location[1];
                    locationTextView.setText("当前位置： 纬度： " + latitude + "\n" +
                    "经度： " + longtitude);
                    break;
                case R.id.baiduMapButton:
                    Intent intent = new Intent(MainActivity.this, BaiduMap_Display.class);
                    intent.putExtra("isNowLocation",true);
                    startActivity(intent);
                    break;
                case R.id.savedLocationButton:
                    Intent intent1 = new Intent(MainActivity.this, BaiduMap_Display.class);
                    String latitudeString = inputLatitude.getText().toString();
                    String longitudeString = inputLongitude.getText().toString();
                    if(latitudeString.equals("") || longitudeString.equals("")){
                        latitude = 0;
                        longtitude = 0;
                    }else{
                        latitude = Double.valueOf(latitudeString);
                        longtitude = Double.valueOf(longitudeString);
                    }
                    intent1.putExtra("isNowLocation", false);
                    intent1.putExtra("latitude", latitude);
                    intent1.putExtra("longitude", longtitude);
                    startActivity(intent1);
                    break;
                case R.id.saveButton:
                    locationNow = new LocationNow();
                    double[] location2 = locationNow.LocationNow(MainActivity.this);
                    String name = inputSaveLocationName.getText().toString();
                    String describe = inputLocationDescribe.getText().toString();
                    Save_and_Read.Save(saveLocationDatabaseHelper,location2,name,describe);
                    break;
                case R.id.readButton:
                    Intent intent2 = new Intent(MainActivity.this, SavedLocation_Display.class);
                    startActivity(intent2);
                    break;
                case R.id.pictureButton:
                    String pictureAddress = Save_and_Read.savePicture(MainActivity.this, MainActivity.this);

                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(locationNow != null){
            locationNow.removeListener();
        }
    }
}
