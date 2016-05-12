package com.lzybetter.simpletravlenotes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;

public class SavedLocation_Display extends Activity {

    private ListView listView;
    private SimpleAdapter simpleAdapter;
    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    private String pictureAddress;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.savedlocation_display);
        listView = (ListView)findViewById(R.id.savedLocationList);
        pictureAddress = "There is no picture";

        cursor = Save_and_Read.Read(this, 0, true);
        if(cursor == null){
            String noName = "没有储存的地址";
            String noDescribe = "没有储存的描述";
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("saveLocationName", noName);
            map.put("saveLocationDescribe", noDescribe);
            map.put("saveLocationPicture",R.drawable.nopicture);
            list.add(map);
            Toast.makeText(SavedLocation_Display.this, "没有保存的信息", Toast.LENGTH_SHORT).show();
        }else{
            if(cursor.moveToFirst()){
                do{
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    String describe = cursor.getString(cursor.getColumnIndex("describe"));
                    pictureAddress = cursor.getString(cursor.getColumnIndex("pictureAddress"));

                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("saveLocationName", name);
                    map.put("saveLocationDescribe", describe);
                    if(pictureAddress.equals("There is no picture")){
                        //如果地址中传入了其他奇怪的东西这里会导致崩溃
                        map.put("saveLocationPicture", R.drawable.defaulpicture);
                    }else{
                        //如何实现让图片填充到整个容器中
                        Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromFile(pictureAddress, 100,100);
                        map.put("saveLocationPicture", bitmap);
                    }
                    list.add(map);
                }while(cursor.moveToNext());
            }
        }
        if(cursor != null) {
            cursor.close();
        }

        simpleAdapter = new SimpleAdapter(this, list, R.layout.savelocationlist,
                new String[] {"saveLocationName","saveLocationDescribe","saveLocationPicture"},
                new int[] {R.id.locationName, R.id.locationDescribe, R.id.locationPicture});

        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String s) {
                if((view instanceof ImageView) && (data instanceof Bitmap)) {
                    ImageView imageView = (ImageView) view;
                    Bitmap bmp = (Bitmap) data;
                    imageView.setImageBitmap(bmp);
                    return true;
                }
                    return false;
            }
        });
        listView.setAdapter(simpleAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    if(cursor != null){
                        Intent intent = new Intent(SavedLocation_Display.this, contextDisplay.class);
                        intent.putExtra("position",position + 1);
                        intent.putExtra("isDisplay", true);
                        startActivity(intent);
                    }
                }
            });
        }


}
