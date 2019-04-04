package com.example.agenda_try3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "mylist.db";
    public static final String TABLE_NAME = "event_table";
    public static final String COL1 = "ID";
    public static final String COL2 = "ITEM";

    public long result;

    public DataBaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createT = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " ITEM1 TEXT)";
        db.execSQL(createT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS" + TABLE_NAME);
        onCreate(db);
    }

    public long addEvents(String event1){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL2, event1);

        result = db.insert(TABLE_NAME, null, cv);

        return result;
    }

    public boolean addData(long res){

        res = result;

        if(res==-1){
            return false;
        }else{
           return true;
        }
    }

    public Cursor getListContents(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data;
    }
}
