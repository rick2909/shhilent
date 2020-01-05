package com.example.shhilent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import java.time.LocalDate;

/**
 * Created by tutlane on 06-01-2018.
 */

public class DbHandler extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "meldingen";
    private static final String TABLE_NAME = "meldingdetails";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_BES = "beschrijving";
    private static final String KEY_DAG = "datum";
    private static final String KEY_TIME = "tijd";
    public DbHandler(Context context){
        super(context,DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TITLE + " TEXT,"
                + KEY_BES + " TEXT,"
                + KEY_DAG + " TEXT,"
                + KEY_TIME + " TEXT)";
        db.execSQL(CREATE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // Drop older table if exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Create tables again
        onCreate(db);
    }
    // **** CRUD (Create, Read, Update, Delete) Operations ***** //

    // Adding new User Details
    void insertMelding(String title, String description, String dag, String time){
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put(KEY_TITLE, title);
        cValues.put(KEY_BES, description);
        cValues.put(KEY_DAG, dag);
        cValues.put(KEY_TIME, time);
        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(TABLE_NAME,null, cValues);
        db.close();
    }
    // Get User Details
    public ArrayList<HashMap<String, String>> getMeldingen(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> meldingList = new ArrayList<>();
        String query = "SELECT " + KEY_TITLE + ", "
                + KEY_BES + ", "
                + KEY_DAG + ", "
                + KEY_TIME + " FROM "+ TABLE_NAME;
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            HashMap<String,String> melding = new HashMap<>();
            melding.put("Title",cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
            melding.put("Beschrijving",cursor.getString(cursor.getColumnIndex(KEY_BES)));
            melding.put("Datum",cursor.getString(cursor.getColumnIndex(KEY_DAG)));
            melding.put("Tijd",cursor.getString(cursor.getColumnIndex(KEY_TIME)));
            meldingList.add(melding);
        }
        return  meldingList;
    }

    public ArrayList<HashMap<String, String>> getLastMelding(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> meldingList = new ArrayList<>();
        String query = "SELECT " + KEY_TITLE + ", "
                + KEY_BES + ", "
                + KEY_DAG + ", "
                + KEY_TIME + " FROM "+ TABLE_NAME + " WHERE " + KEY_ID + " IN ( SELECT max( " + KEY_ID + " ) FROM " + TABLE_NAME + " )";
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            HashMap<String,String> melding = new HashMap<>();
            melding.put("Title",cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
            melding.put("Beschrijving",cursor.getString(cursor.getColumnIndex(KEY_BES)));
            melding.put("Datum",cursor.getString(cursor.getColumnIndex(KEY_DAG)));
            melding.put("Tijd",cursor.getString(cursor.getColumnIndex(KEY_TIME)));
            meldingList.add(melding);
        }
        return  meldingList;
    }

    public int getAmount(){
        // pak de datum van vandaag
        Date localDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String date = df.format(localDate);

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+ TABLE_NAME + " WHERE " + KEY_DAG + " = '" + date + "'";
        Cursor cursor = db.rawQuery(query,null);
        int amount = cursor.getCount();

        return  amount;
    }

    // Get User Details based on userid
    public ArrayList<HashMap<String, String>> GetMeldingById(int meldingId){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> meldingList = new ArrayList<>();
        String query = "SELECT name, location, designation FROM "+ TABLE_NAME;
        Cursor cursor = db.query(TABLE_NAME, new String[]{KEY_TITLE, KEY_BES, KEY_DAG}, KEY_ID+ "=?",new String[]{String.valueOf(meldingId)},null, null, null, null);
        if (cursor.moveToNext()){
            HashMap<String,String> melding = new HashMap<>();
            melding.put("Title",cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
            melding.put("Datum",cursor.getString(cursor.getColumnIndex(KEY_DAG)));
            melding.put("Beschrijving",cursor.getString(cursor.getColumnIndex(KEY_BES)));
            melding.put("Tijd",cursor.getString(cursor.getColumnIndex(KEY_TIME)));
            meldingList.add(melding);
        }
        return  meldingList;
    }
    // Delete User Details
    public void DeleteMelding(int meldingId){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID+" = ?",new String[]{String.valueOf(meldingId)});
        db.close();
    }
}
