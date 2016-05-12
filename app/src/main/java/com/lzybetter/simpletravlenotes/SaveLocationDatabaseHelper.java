package com.lzybetter.simpletravlenotes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by lzybetter on 2016/4/21.
 */
public class SaveLocationDatabaseHelper extends SQLiteOpenHelper{

    public static final String CREATE_SAVELOCATION = "create table Savedlocation ("
            + "id integer primary key autoincrement, "
            + "name text, "
            + "latitude real, "
            + "longitude real, "
            + "describe text, "
            + "pictureAddress text)";

    private Context mContext;

    public SaveLocationDatabaseHelper(Context context, String name,
                                       SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
        mContext = context;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SAVELOCATION);
    }
}
