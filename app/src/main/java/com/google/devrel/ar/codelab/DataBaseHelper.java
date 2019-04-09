package com.example.agenda_try3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "mylist.db";
    public static final String TABLE_NAME = "event_table";
    public static final String COL1 = "ID";
    public static final String COL2 = "DAY";
    public static final String COL3 = "ITEM1";
    public SQLiteDatabase db;

    public long result;

    public DataBaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " + "DAY TEXT, "+ "ITEM1 TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    public boolean addData(String item1){
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL3, item1);

        long result = db.insert(TABLE_NAME, null, cv);

        if(result == -1)
        {return false;}
        else
        {return true;}
    }

    public Cursor getListContents(){
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data;
    }
}
