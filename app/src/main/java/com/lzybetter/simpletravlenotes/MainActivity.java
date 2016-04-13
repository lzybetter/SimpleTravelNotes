package com.lzybetter.simpletravlenotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    private Button button, baiduMapButton;
    private TextView locationTextView;
    private double latitude, longtitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        locationTextView = (TextView)findViewById(R.id.textView);
        button = (Button)findViewById(R.id.button);
        baiduMapButton = (Button)findViewById(R.id.baiduMapButton);

        ButtonListener buttonListener = new ButtonListener();
        button.setOnClickListener(buttonListener);
        baiduMapButton.setOnClickListener(buttonListener);
    }

    class ButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.button:
                    LocationNow locationNow = new LocationNow();
                    double[] location = locationNow.LocationNow(MainActivity.this);
                    latitude = location[0];
                    longtitude = location[1];
                    locationTextView.setText("当前位置： 纬度： " + latitude + "\n" +
                    "经度： " + longtitude);
                    break;
                case R.id.baiduMapButton:
                    Intent intent = new Intent(MainActivity.this, BaiduMap_Display.class);
                    startActivity(intent);
            }
        }
    }
}
