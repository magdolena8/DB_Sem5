package com.begdev.lab_10.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context){
        super(context, DBContract.DB_NAME, null, 1);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DBContract.SQL_CREATE_GROUPS_TABLE);
        sqLiteDatabase.execSQL(DBContract.SQL_CREATE_STUDENTS_TABLE);
    }

    @Override
    public void onUpgrade(@NonNull SQLiteDatabase sqLiteDatabase, int i, int i1) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("PRAGMA foreign_keys=ON");
    }

    public long addGroup(Group group){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.DBGroupsTable.COLUMN_NAME_FACULTY, group.getFaculty());
        values.put(DBContract.DBGroupsTable.COLUMN_NAME_COURSE, group.getCourse());
        values.put(DBContract.DBGroupsTable.COLUMN_NAME_NAME, group.getGroupName());
        return db.insert(DBContract.DBGroupsTable.TABLE_NAME, null, values);
    }

    public int addGroupHead(Group group, Student student){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.DBGroupsTable.COLUMN_NAME_HEAD, student.getId());
//        this.getGroups();
        return db.update(DBContract.DBGroupsTable.TABLE_NAME, values, "_id == ?", new String[]{Integer.toString(group.getId())});
    }

    public int deleteGroup(Group group){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys=ON");
        return db.delete(DBContract.DBGroupsTable.TABLE_NAME, "_id == ?", new String[]{Integer.toString(group.getId())});

    }

    public long addStudent(Student student){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.DBStudentsTable.COLUMN_NAME_IDGROUP, student.getGroupId());
        values.put(DBContract.DBStudentsTable.COLUMN_NAME_NAME, student.getName());
        return db.insert(DBContract.DBStudentsTable.TABLE_NAME, null, values);
    }

    public void getGroups(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DBContract.DBGroupsTable.TABLE_NAME, null, null, null, null, null, null);
        Group.groups.clear();
        while(cursor.moveToNext()){
            Group.groups.add(new Group(cursor.getInt(0),cursor.getString(1), cursor.getInt(2), cursor.getString(3), cursor.getInt(4)));
        }
    }

    public ArrayList<Student> getStudentsByGroup(Group group){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DBContract.DBStudentsTable.TABLE_NAME, null, DBContract.DBStudentsTable.COLUMN_NAME_IDGROUP+" == "+group.getId(), null, null, null, null);
        ArrayList<Student> studentList = new ArrayList<>();
        while(cursor.moveToNext()){
            Student student = new Student(cursor.getInt(0),cursor.getInt(1), cursor.getString(2));
            studentList.add(student);
            if(group.getHeadId() == student.getId()){
                student.isHead = true;
            }
        }
        return studentList;
    }

}
