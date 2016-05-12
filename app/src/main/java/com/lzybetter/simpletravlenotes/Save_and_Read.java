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
    double[] location, String name, String describe, String pictureAddress){
        SQLiteDatabase db = saveLocationDatabaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        double latitude = location[0];
        double longtitude = location[1];
        values.put("name", name);
        values.put("latitude", latitude);
        values.put("longitude", longtitude);
        values.put("describe", describe);
        values.put("pictureAddress", pictureAddress);
        db.insert("Savedlocation", null, values);
    }

    public static Cursor Read(Context context, int position, boolean isAll){
        String packageName =  context.getPackageName();
        String DB_Path = "/data/data/" + packageName + "/databases/SaveLocation.db";
        File dir = new File(DB_Path);
        if(dir.exists()){
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_Path, null);
            if(isAll){
                Cursor cursor = db.query("Savedlocation",null, null, null, null, null, null);
                return cursor;
            }else {
                String selection = "id = ?";
                String[] selectionArgs =  {"" + position};
                Cursor cursor = db.query("Savedlocation", null, selection , selectionArgs, null, null, null);
                return cursor;
            }
        }else{
            return  null;
        }

    }

    public static boolean Update(Context context,String name, String describe, String pictureAddress){
        boolean isSu = false;
        String packageName =  context.getPackageName();
        String DB_Path = "/data/data/" + packageName + "/databases/SaveLocation.db";
        File dir = new File(DB_Path);
        if(dir.exists()){
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_Path, null);
            ContentValues values = new ContentValues();
            values.put("name", name);
            db.update("Savedlocation",values,"name=?",new String[] {"name"});
            values.clear();
            values.put("describe",describe);
            db.update("Savedlocation",values,"name=?",new String[] {"describe"});
            values.clear();
            values.put("pictureAddress",pictureAddress);
            db.update("Savedlocation",values,"name=?",new String[] {"pictureAddres"});
            isSu = true;
        }
        return isSu;
    }

}
