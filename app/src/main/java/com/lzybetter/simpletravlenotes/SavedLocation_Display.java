package com.lzybetter.simpletravlenotes;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SavedLocation_Display extends Activity {

    private ListView listView;
    private SimpleAdapter simpleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.savedlocation_display);

        listView = (ListView)findViewById(R.id.savedLocationList);

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("saveLocationName", "TestLocation1");
        map.put("saveLocationDescribe", "TestDescribe1");
        map.put("saveLocationPicture", R.drawable.testpicture1);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("saveLocationName", "TestLocation2");
        map.put("saveLocationDescribe", "TestDescribe2");
        map.put("saveLocationPicture", R.drawable.testpicture2);
        list.add(map);

        simpleAdapter = new SimpleAdapter(this, list, R.layout.savelocationlist,
                new String[] {"saveLocationName","saveLocationDescribe","saveLocationPicture"},
                new int[] {R.id.locationName, R.id.locationDescribe, R.id.locationPicture});
        listView.setAdapter(simpleAdapter);
    }
}
