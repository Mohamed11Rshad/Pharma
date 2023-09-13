package com.mo.medreminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class ALARMDB extends SQLiteOpenHelper {


    public static final String DB_NAME = "alarm_db";
    public static final int DB_VERSION = 2;
    public static final String AL_TABLE_NAME = "alarm";
    public static final String AL_CLN_ID = "al_id";
    public static final String AL_CLN_BRAND_NAME = "al_brand_name";
    public static final String AL_CLN_DOSE = "al_dose";
    public static final String AL_CLN_TIME = "al_time";
    public static final String AL_CLN_REQ = "al_req";




    public ALARMDB(Context context){
        super(context , DB_NAME,null,DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE "+AL_TABLE_NAME+" ("+AL_CLN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ""+AL_CLN_BRAND_NAME+" TEXT, "+AL_CLN_DOSE+" TEXT , "+AL_CLN_TIME+" TEXT , "+AL_CLN_REQ+" INTEGER )");



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    //  ALARM DATABASE
    public boolean insertAlarm(Alarm alarm){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AL_CLN_BRAND_NAME , alarm.getName());
        values.put(AL_CLN_DOSE , alarm.getDose());
        values.put(AL_CLN_TIME, alarm.getTime());
        values.put(AL_CLN_REQ, alarm.getReqCode());

        long result = db.insert(AL_TABLE_NAME ,null ,values);
        return (result!=-1);
    }

    //Delete
    public void deleteAlarm(int req){
        String query = "DELETE FROM " + AL_TABLE_NAME + " WHERE " + AL_CLN_REQ + " = ?";
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(query, new String[] { String.valueOf(req) });

    }



    public ArrayList<Alarm>getAllALarms(){
        ArrayList<Alarm> alarms = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+AL_TABLE_NAME , null);
        if (c.moveToFirst())
        {
            do {
                String name = c.getString(c.getColumnIndexOrThrow(AL_CLN_BRAND_NAME));
                String time = c.getString(c.getColumnIndexOrThrow(AL_CLN_TIME));
                String dose = c.getString(c.getColumnIndexOrThrow(AL_CLN_DOSE));
                int reqCode = c.getInt(c.getColumnIndexOrThrow(AL_CLN_REQ));
                Alarm alarm = new Alarm(name , dose , time , reqCode);

                alarms.add(alarm);
            }
            while (c.moveToNext());
            c.close();
        }
        return alarms;
    }

}
