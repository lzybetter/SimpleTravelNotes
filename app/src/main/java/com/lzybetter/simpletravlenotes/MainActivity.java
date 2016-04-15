package com.lzybetter.simpletravlenotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

    private Button button, baiduMapButton, saveLocationButton;
    private TextView locationTextView;
    private double latitude, longtitude;
    private LocationNow locationNow;
    private EditText inputLatitude, inputLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        locationTextView = (TextView)findViewById(R.id.textView);
        button = (Button)findViewById(R.id.button);
        baiduMapButton = (Button)findViewById(R.id.baiduMapButton);
        saveLocationButton = (Button)findViewById(R.id.savedLocationButton);
        inputLatitude = (EditText)findViewById(R.id.inputLatitude);
        inputLongitude = (EditText)findViewById(R.id.inputLongitude);

        ButtonListener buttonListener = new ButtonListener();
        button.setOnClickListener(buttonListener);
        baiduMapButton.setOnClickListener(buttonListener);
        saveLocationButton.setOnClickListener(buttonListener);
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
                    latitude = Double.valueOf(latitudeString);
                    longtitude = Double.valueOf(longitudeString);
                    intent1.putExtra("isNowLocation", false);
                    intent1.putExtra("latitude", latitude);
                    intent1.putExtra("longitude", longtitude);
                    startActivity(intent1);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationNow.removeListener();
    }
}
