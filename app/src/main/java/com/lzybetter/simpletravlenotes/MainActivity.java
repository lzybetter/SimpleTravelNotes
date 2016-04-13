package com.lzybetter.simpletravlenotes;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TextView textView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.textView);
        button = (Button)findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocationNow locationNow = new LocationNow();
                double[] location = locationNow.LocationNow(MainActivity.this);
                double latitude = location[0];
                double longitude = location[1];
                if( latitude == 0 && longitude == 0){
                    textView.setText("未开启定位");
                }else{
                    textView.setText("现在的坐标是: " + latitude + " " + longitude);}
            }
        });

    }
}
