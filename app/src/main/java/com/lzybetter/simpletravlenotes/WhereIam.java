package com.lzybetter.simpletravlenotes;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by lzybetter on 2016/4/19.
 */
public class WhereIam {

    private final static int SHOW_LOCATION_NAME = 1;
    private String locatonName_Response;
    private TextView locationName_Show;


    public interface HttpCallbackListener{
        void onFinish(String response);

        void onError(Exception e);
    }


    public void whereIam(final double[] location, final HttpCallbackListener listener){

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try{
                    double latitude = location[0];
                    double longitude = location[1];
                    URL url= new URL("http://maps.googleapis.com/maps/api/geocode/json?latlng=" +
                    latitude + "," + longitude + "&sensor=false");
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while((line = reader.readLine()) != null){
                        response.append(line);
                    }
                    if(listener != null){
                        listener.onFinish(response.toString());
                    }
                }catch(Exception e){
                    if(listener != null){
                        listener.onError(e);
                    }
                }finally {
                    if(connection != null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

}
