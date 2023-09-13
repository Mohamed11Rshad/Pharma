package com.mo.medreminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class MyDatabase extends SQLiteOpenHelper {

    public static final String DB_NAME = "medicine_db";
    public static final int DB_VERSION = 2;
    public static final String MED_TABLE_NAME = "medicine";
    public static final String MED_CLN_ID = "id";
    public static final String MED_CLN_BRAND_NAME = "brand_name";
    public static final String MED_CLN_STRENGTH = "strength";
    public static final String MED_CLN_PHOTO = "photo";
    public static final String MED_CLN_INDICATIONS = "indications";

    public MyDatabase(Context context){
        super(context , DB_NAME,null,DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+MED_TABLE_NAME+" ("+MED_CLN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ""+MED_CLN_BRAND_NAME+" TEXT, "+MED_CLN_STRENGTH+" TEXT , "+MED_CLN_PHOTO+" INTEGER , "+MED_CLN_INDICATIONS+" TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //  MED DATABASE
    //insert Medicine
    public boolean insertMedicine(Medicine medicine){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MED_CLN_BRAND_NAME , medicine.getBrandName());
        values.put(MED_CLN_STRENGTH , medicine.getStrength());
        values.put(MED_CLN_PHOTO, medicine.getPhoto());
        values.put(MED_CLN_INDICATIONS , medicine.getIndications());
        long result = db.insert(MED_TABLE_NAME ,null ,values);
        return (result!=-1);
    }

    //get all Brand Names
    public ArrayList<String>getAllMedicines(){
        ArrayList<String> medicines = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+MED_TABLE_NAME , null);
        if (c.moveToFirst())
        {
            do {
                String brandName = c.getString(c.getColumnIndexOrThrow(MED_CLN_BRAND_NAME));
                medicines.add(brandName);
            }
            while (c.moveToNext());
            c.close();
        }
        return medicines;
    }

    //get all strengthes for the brand
    public ArrayList<String>getMedicineByName(String name) {
        ArrayList<String> medicines = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+MED_TABLE_NAME+" WHERE "+MED_CLN_BRAND_NAME+"=?"  , new String[]{name});
        if (c.moveToFirst())
        {
            do {
                String strength = c.getString(c.getColumnIndexOrThrow(MED_CLN_STRENGTH));
                medicines.add(strength);
            }
            while (c.moveToNext());
            c.close();
        }
        return medicines;
    }

    //get medicine Information
    public ArrayList<Medicine>getMedicine(String name , String strength) {
        ArrayList<Medicine> medicines = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+MED_TABLE_NAME+" WHERE "+MED_CLN_BRAND_NAME+"=? AND "+MED_CLN_STRENGTH+"=?  "  , new String[]{name , strength});
        if (c.moveToFirst())
        {
            do {
                String brandName = c.getString(c.getColumnIndexOrThrow(MED_CLN_BRAND_NAME));
                String brandStrength = c.getString(c.getColumnIndexOrThrow(MED_CLN_STRENGTH));
                int brandPhoto = c.getInt(c.getColumnIndexOrThrow(MED_CLN_PHOTO));
                String brandIndications = c.getString(c.getColumnIndexOrThrow(MED_CLN_INDICATIONS));
                Medicine med = new Medicine(brandName , brandStrength , brandPhoto , brandIndications);
                medicines.add(med);
            }
            while (c.moveToNext());
            c.close();
        }
        return medicines;
    }

    //Delete
    public void deleteAllMedicines(){
        String query = "DELETE FROM "+MED_TABLE_NAME ;
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(query);
    }


}
