package com.lzybetter.simpletravlenotes;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by lzybetter on 2016/4/20.
 */
public class Save_and_Read {


    public static void Save(SaveLocationDatabaseHelper saveLocationDatabaseHelper,
    double[] location, String name, String describe){
        SQLiteDatabase db = saveLocationDatabaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        double latitude = location[0];
        double longtitude = location[1];
        values.put("name", name);
        values.put("latitude", latitude);
        values.put("longitude", longtitude);
        values.put("describe", describe);
        db.insert("Savedlocation", null, values);
    }

    public static Cursor Read(Context context){
        String packageName =  context.getPackageName();
        String DB_Path = "/data/data/" + packageName + "/databases/SaveLocation.db";
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_Path, null);
        Cursor cursor = db.query("Savedlocation",null, null, null, null, null, null);
        return cursor;
    }

    public static String savePicture(Context context, Activity activity){
        Uri imageUri;
        int nameNumber = (int)Math.random()*900000+100000;
        String pictureName = "location_Picture" + "_" + nameNumber + ".jpg";
        String address = Environment.getExternalStorageDirectory().getAbsolutePath() + "/simpletravlenots/";
        String pictureAddress = address + pictureName;
        File destDir = new File(address);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        File outputImage = new File(address, pictureName);
        try {
            if(outputImage.exists()){
                outputImage.delete();
            }
            outputImage.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
        }
        imageUri = Uri.fromFile(outputImage);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        context.startActivity(intent);
        return pictureAddress;
    }



}
