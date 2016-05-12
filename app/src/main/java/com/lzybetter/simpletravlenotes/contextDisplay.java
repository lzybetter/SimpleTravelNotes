package com.lzybetter.simpletravlenotes;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class contextDisplay extends Activity {

    private Button baiduMapShowButton;
    private TextView locationNameShow, locationDescribeShow;
    private ImageView locationPictureShow;
    private double latitude, longtitude;
    private int position;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_context_display);;

        baiduMapShowButton = (Button)findViewById(R.id.baiduMapShow);
        locationNameShow = (TextView)findViewById(R.id.savedLocationName);
        locationDescribeShow = (TextView)findViewById(R.id.savedLocationDescribe);
        locationPictureShow = (ImageView)findViewById(R.id.savedLocationPicture);

        intent = getIntent();
        boolean isDisplay = intent.getBooleanExtra("isDisplay", false);

        if(isDisplay){
            ReadDataBase();
        }else{
            DisplaySaveResult();
        }

        ButtonOnClickListener buttonOnClickListener = new ButtonOnClickListener();
        baiduMapShowButton.setOnClickListener(buttonOnClickListener);
    }

    class ButtonOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(contextDisplay.this, BaiduMap_Display.class);
            intent.putExtra("isNowLocation",false);
            intent.putExtra("latitude", latitude);
            intent.putExtra("longtitude",longtitude);
            intent.putExtra("position",position);
            startActivity(intent);
        }
    }

    private void ReadDataBase(){

        position = intent.getIntExtra("position",0);

        Cursor cursor = Save_and_Read.Read(this, position, false);

        if(cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String describe = cursor.getString(cursor.getColumnIndex("describe"));
            String pictureAddress = cursor.getString(cursor.getColumnIndex("pictureAddress"));
            show(name, describe, pictureAddress);
        }

        latitude = cursor.getDouble(cursor.getColumnIndex("latitude"));
        longtitude = cursor.getDouble(cursor.getColumnIndex("longitude"));
    }

    private void DisplaySaveResult(){
        double[] location = intent.getDoubleArrayExtra("location");
        latitude = location[0];
        longtitude = location[1];
        String name = intent.getStringExtra("name");
        String describe = intent.getStringExtra("describe");
        String pictureAddress = intent.getStringExtra("pictureAddress");
        show(name, describe, pictureAddress);
    }

    private void show(String name, String describe, String pictureAddress){

        locationNameShow.setText(name);
        locationDescribeShow.setText(describe);
        if (pictureAddress.equals("There is no picture")) {
            locationPictureShow.setImageResource(R.drawable.nopicture);
        } else {
            int pictureWidth = locationPictureShow.getWidth();
            int pictureHeight = locationPictureShow.getHeight();
            Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromFile(pictureAddress, pictureWidth, pictureHeight);
            locationPictureShow.setImageBitmap(bitmap);
        }
    }
}
