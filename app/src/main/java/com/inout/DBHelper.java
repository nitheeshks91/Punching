package com.inout;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by 08468 on 6/9/2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDB.db";
    public static final String TABLE_NAME = "timetable";
    public static final String CONTACTS_COLUMN_ID = "id";
    public static final String COLUMN_CURRENTDATE = "currentdate";
    public static final String COLUMN_CURRENTTIME = "currentintime";
    public static final String COLUMN_CURRENTOUTTIME = "currentouttime";
    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table " + TABLE_NAME +
                        " (" + CONTACTS_COLUMN_ID + " integer primary key, " + COLUMN_CURRENTDATE + " text," +
                        COLUMN_CURRENTTIME + " text, " + COLUMN_CURRENTOUTTIME + " text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String date, String time, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_CURRENTDATE, date);
        contentValues.put(COLUMN_CURRENTTIME, time);
        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean updateContact(Integer id, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_CURRENTOUTTIME, time);
        db.update(TABLE_NAME, contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }


    public Cursor getAllRecord() {
        SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
        String dateObj = curFormater.format(new Date());
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " where " + COLUMN_CURRENTDATE + " = ?", new String[]{dateObj});
        return res;
    }

    public void deleteAllRecord() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_NAME);
    }


    public Cursor getLastRowId() {
        SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
        String dateObj = curFormater.format(new Date());
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT MAX(id) max FROM " + TABLE_NAME + " where " + COLUMN_CURRENTDATE + " = ?", new String[]{dateObj});
        return res;
    }

    public Cursor getLastRowOutTime(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT currentouttime FROM " + TABLE_NAME + " where " + CONTACTS_COLUMN_ID + " =?", new String[]{Integer.toString(id)});
        return res;
    }


}
