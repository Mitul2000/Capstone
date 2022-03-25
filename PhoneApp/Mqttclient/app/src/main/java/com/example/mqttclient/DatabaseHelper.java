package com.example.mqttclient;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "Userdata.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "user_device";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "device_name";
    private static final String COLUMN_DEVICE_ID = "device_id";
    private static final String COLUMN_STATUS = "device_status";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                        " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_NAME + " TEXT, " +
                        COLUMN_DEVICE_ID + " TEXT, " +
                        COLUMN_STATUS + " TEXT);";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }
    public void adddevice(String name, String deviceId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_DEVICE_ID, deviceId);
        cv.put(COLUMN_STATUS, "active");
        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1){
            Toast.makeText(context, "Insert failed", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Insert success", Toast.LENGTH_SHORT).show();

        }
    }

    Cursor readAllData(){
        String query = "SELECT * FROM "+ TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db!= null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public void updateData(String row_id, String dname){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, dname);

        long result = db.update(TABLE_NAME, cv, "device_id=?",new String[]{row_id});
        if(result ==-1){
            Toast.makeText(context, "Failed update", Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(context, "Success update", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteOneRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "device_id=?",new String[]{row_id});
        if(result ==-1){
            Toast.makeText(context, "Failed delete", Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(context, "Success delete", Toast.LENGTH_SHORT).show();
        }

    }
}
