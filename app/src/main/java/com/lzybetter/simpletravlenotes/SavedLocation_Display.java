package com.lzybetter.simpletravlenotes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SavedLocation_Display extends Activity {

    private ListView listView;
    private SimpleAdapter simpleAdapter;
    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    private String pictureAddress;
    private ImageView imageView;
    private int width,height;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.savedlocation_display);

        listView = (ListView)findViewById(R.id.savedLocationList);
        imageView = (ImageView)findViewById(R.id.locationPicture);
        width = imageView.getWidth();
        height = imageView.getHeight();
        Cursor cursor = Save_and_Read.Read(this);

        if(cursor.moveToFirst()){
            do{
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String describe = cursor.getString(cursor.getColumnIndex("describe"));
                pictureAddress = cursor.getString(cursor.getColumnIndex("pictureAddress"));
                Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromFile(pictureAddress,width,height);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("saveLocationName", name);
                map.put("saveLocationDescribe", describe);
                map.put("saveLocationPicture", bitmap);
                list.add(map);
            }while(cursor.moveToNext());
        }

        cursor.close();

        simpleAdapter = new SimpleAdapter(this, list, R.layout.savelocationlist,
                new String[] {"saveLocationName","saveLocationDescribe","saveLocationPicture"},
                new int[] {R.id.locationName, R.id.locationDescribe, R.id.locationPicture});
        listView.setAdapter(simpleAdapter);
    }
}
