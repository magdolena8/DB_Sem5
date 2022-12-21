package com.begdev.lab_11;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class UniverContentProvider extends ContentProvider {
    static final String AUTHORITY = "com.begdev.provider.university";
    static final int URI_GROUPS_ALL = 102;
    static final int URI_GROUPS_SINGLE = 101;
    static final int URI_STUDENTS_ALL = 202;
    static final int URI_STUDENTS_SINGLE = 201;

    DBHelper dbHelper;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "groups", URI_GROUPS_ALL);
        uriMatcher.addURI(AUTHORITY, "groups" + "/#", URI_GROUPS_SINGLE);
        uriMatcher.addURI(AUTHORITY, "student" + "/#", URI_STUDENTS_ALL);
        uriMatcher.addURI(AUTHORITY, "student" + "/#/#", URI_STUDENTS_SINGLE);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM GROUPS", null);
        switch (uriMatcher.match(uri)) {
            case URI_GROUPS_ALL: {
                cursor = db.rawQuery("SELECT _id, (SELECT NAME FROM STUDENT WHERE STUDENT._id = G.HEAD), (SELECT COUNT(*) FROM STUDENT WHERE STUDENT.IDGROUP = G._id) FROM GROUPS G GROUP BY _id", null);
                return cursor;
            }
            case URI_GROUPS_SINGLE: {
                String groupID = uri.getLastPathSegment();
                cursor = db.rawQuery("SELECT (SELECT NAME FROM STUDENT WHERE STUDENT._id = G.HEAD),(SELECT COUNT(*) FROM STUDENT WHERE STUDENT.IDGROUP=G._id) FROM GROUPS G WHERE G._id=?", new String[]{groupID});
                return cursor;
            }
            case URI_STUDENTS_ALL: {
                String groupID = uri.getLastPathSegment();
                cursor = db.rawQuery("SELECT _id, NAME FROM STUDENT WHERE IDGROUP=?", new String[]{groupID});
                return cursor;
            }
            case URI_STUDENTS_SINGLE: {
                List<String> idList = uri.getPathSegments();
                cursor = db.rawQuery("SELECT S._id, S.NAME, (SELECT AVG(P.MARK) FROM PROGRESS P WHERE P.IDSTUDENT = S._id) FROM STUDENT S WHERE S.IDGROUP= ? AND S._id= ?", new String[]{idList.get(1).toString(), idList.get(2).toString()});
                cursor.moveToNext();
                cursor.getString(1);
                return cursor;
            }
        }
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case URI_GROUPS_ALL: {
                String groupID = uri.getLastPathSegment();
                db.insert(DBContract.DBGroupsTable.TABLE_NAME, null, contentValues);
            }
            case URI_GROUPS_SINGLE: {
                List<String> idList = uri.getPathSegments();
                db.insert(DBContract.DBStudentsTable.TABLE_NAME, null, contentValues);
            }
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys=ON");
        switch (uriMatcher.match(uri)) {
            case URI_GROUPS_ALL: {
                int result = db.delete("GROUPS", null, null);
                return result;
            }
            case URI_GROUPS_SINGLE: {
                String groupID = uri.getLastPathSegment();
                int result = db.delete("GROUPS", "_id = " + groupID, null);
                return result;
            }
            case URI_STUDENTS_ALL: {
                String groupID = uri.getLastPathSegment();
                int result = db.delete("STUDENT", "IDGROUP = " + groupID, null);
                return result;
            }
            case URI_STUDENTS_SINGLE: {
                String idStudent = uri.getLastPathSegment();
                int result = db.delete("STUDENT", "_id = " + idStudent, null);
                return result;
            }
        }
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys = ON");
        int updateResult = 0;
        Log.d("Provider.Update", "query: " + uri);
        switch (uriMatcher.match(uri)) {
            case URI_GROUPS_SINGLE: {
                String groupID = uri.getLastPathSegment();
                updateResult = db.update(DBContract.DBGroupsTable.TABLE_NAME, contentValues, "_id=" + groupID, null);
                return updateResult;
            }
            case URI_STUDENTS_ALL: {
                String studentID = uri.getLastPathSegment();
                updateResult = db.update(DBContract.DBStudentsTable.TABLE_NAME, contentValues, "_id=" + studentID, null);
                return updateResult;
            }
        }
        return updateResult;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }
}
