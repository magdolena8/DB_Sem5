package com.begdev.lab_9;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;

public class CustomDBHelper extends SQLiteOpenHelper {
    private final String TABLE_NAME = "Lab_DB";
    private final String DATABASE_NAME = "myDB.db";

    private final String COL1 = "ID";
    private final String COL2 = "F";
    private final String COL3 = "T";

    private final String CREATE_TABLE = "create table if not exists " + TABLE_NAME + "(" +
            COL1 + " INTEGER primary key autoincrement," +
            COL2 + " REAL NOT NULL, " +
            COL3 + " TEXT NOT NULL " + ");";

    public CustomDBHelper(Context context) {
        super(context, "myDB.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean insertRow(@Nullable Integer i, @NonNull float f, @NonNull String t) throws SQLiteConstraintException {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            if (i != null) {
                values.put(COL1, i);
            }
            values.put(COL2, f);
            values.put(COL3, t);
            long rowId = db.insert(TABLE_NAME, null, values);
            Log.d("Lab_9 insert", String.format("rowid = %d", rowId));
            return rowId == -1 ? false : true;
        } catch (SQLiteConstraintException e) {
            Log.d("Lab_9 insert", "Error");
            return false;
        }
    }

    public DBSet select(@NonNull int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COL1, COL2, COL3}, COL1 + " == " + id, null, null, null, null);
        if (cursor.getCount() == 0) {
            return null;
        }
        cursor.moveToFirst();
        Float F = cursor.getFloat(1);
        String T = cursor.getString(2);
        DBSet result = new DBSet(id, F, T);
        cursor.close();
        return result;
    }

    public DBSet selectRaw(@NonNull Integer id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Lab_DB WHERE id = ?", new String[]{id.toString()});
        if (cursor.getCount() == 0) {
            return null;
        }
        cursor.moveToFirst();
        Float F = cursor.getFloat(1);
        String T = cursor.getString(2);
        DBSet result = new DBSet(id, F, T);
        cursor.close();
        return result;
    }

    public boolean update(@NonNull Integer id, @Nullable Float f, @Nullable String t) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL2, f);
        values.put(COL3, t);
        return db.update(TABLE_NAME, values, "ID == ?", new String[]{id.toString()}) == 0 ?
                false :
                true;
    }

    public boolean delete(@NonNull Integer id){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        return db.delete(TABLE_NAME, "ID == ?", new String[]{id.toString()}) == 0 ?
                false :
                true;
    }
}
