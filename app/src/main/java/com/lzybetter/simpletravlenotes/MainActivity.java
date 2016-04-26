package com.lzybetter.simpletravlenotes;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class MainActivity extends Activity {

    private Button currentLocation, savedLocation;
    private boolean isNowLocation;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        currentLocation = (Button)findViewById(R.id.currentLocation);
        savedLocation = (Button)findViewById(R.id.savedLocation);

        ButtonOnClickListener buttonOnClickListener = new ButtonOnClickListener();
        currentLocation.setOnClickListener(buttonOnClickListener);
        savedLocation.setOnClickListener(buttonOnClickListener);
    }

    class ButtonOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.currentLocation:
                    isNowLocation = true;
                    intent = new Intent(MainActivity.this, BaiduMap_Display.class);
                    intent.putExtra("isNowLocation", isNowLocation);
                    startActivity(intent);
                    break;
                case R.id.savedLocation:
                    intent = new Intent(MainActivity.this, SavedLocation_Display.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    }
}
