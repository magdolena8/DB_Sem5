package com.begdev.lab_11;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "Lab11.db";
    private static final int DB_VERSION = 1;
    private static final String TAG = "DBHelper";

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DBContract.SQL_CREATE_FACULTY_TABLE);
        sqLiteDatabase.execSQL(DBContract.SQL_CREATE_SUBJECT_TABLE);
        sqLiteDatabase.execSQL(DBContract.SQL_CREATE_GROUPS_TABLE);
        sqLiteDatabase.execSQL(DBContract.SQL_CREATE_STUDENTS_TABLE);
        try {
            sqLiteDatabase.execSQL(DBContract.SQL_CREATE_PROGRESS_TABLE);
        }
        catch(SQLException e){e.printStackTrace();}
        fillTables(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//        sqLiteDatabase.execSQL(DBContract.SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    private void fillTables(SQLiteDatabase sqLiteDatabase){
        sqLiteDatabase.execSQL(DBContract.SQL_FILL_FACULTY);
        sqLiteDatabase.execSQL(DBContract.SQL_FILL_SUBJECT);
        sqLiteDatabase.execSQL(DBContract.SQL_FILL_GROUPS);
        sqLiteDatabase.execSQL(DBContract.SQL_FILL_STUDENTS);
        sqLiteDatabase.execSQL(DBContract.SQL_FILL_PROGRESS);

        sqLiteDatabase.execSQL(DBContract.createExamdateIndex);
        sqLiteDatabase.execSQL(DBContract.createMarkIndex);
        sqLiteDatabase.execSQL(DBContract.createViewSubjectQuery);

        sqLiteDatabase.execSQL(DBContract.createTriggerInsertStudent);
        sqLiteDatabase.execSQL(DBContract.createTriggerDeleteStudent);
        sqLiteDatabase.execSQL(DBContract.createTriggerUpdateSubjectView);
    }

    public ArrayList<Integer> getGroupsList(){
        ArrayList<Integer> result = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = String.format("select %s from %s", DBContract.DBGroupsTable.COLUMN_NAME_IDGROUP, DBContract.DBGroupsTable.TABLE_NAME);
        Cursor cursor  = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                result.add(cursor.getInt(0));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return result;
    }
    public ArrayList<String> getSubjectsList() {
        ArrayList<String> result = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = String.format("select %s from %s", DBContract.DBSubjectTable.COLUMN_NAME_SUBJECT, DBContract.DBSubjectTable.TABLE_NAME);
        Cursor cursor  = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                result.add(cursor.getString(0));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return result;
    }

    public ArrayList<String> getFacultiesList() {
        ArrayList<String> result = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = String.format("select %s from %s", DBContract.DBFacultyTable.TABLE_NAME, DBContract.DBFacultyTable.TABLE_NAME);
        Cursor cursor  = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                result.add(cursor.getString(0));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return result;
    }

    public ArrayList<String> getGroupStudents(Integer isGroup){
        ArrayList<String> result = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = String.format("select STUDENT._id, STUDENT.NAME from Student inner join GROUPS on STUDENT.IDGROUP = GROUPS._id where GROUPS._id = %s", isGroup.toString());
        Cursor cursor  = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                result.add(String.format("%s - %s",cursor.getInt(0), cursor.getString(1)));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return result;
    }

    public boolean deleteStudent(Integer idStudent){
        SQLiteDatabase db = this.getReadableDatabase();
        try{
            db.delete(DBContract.DBStudentsTable.TABLE_NAME, "_id = "+ idStudent.toString(),null);
        }
        catch (SQLException sqlException){
            Log.d(TAG, "Ошибка при удалении");
            return false;
        }
        return true;
    }

    public boolean addStudent(Integer idGroup, String nameStudent){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("IDGROUP", idGroup);
        values.put("NAME", nameStudent);
        try{
            db.execSQL("insert into STUDENT(IDGROUP, NAME, BIRTHDATE) values ("+idGroup+", \""+nameStudent+"\", \"01-01-01\")");
        }
        catch (SQLException e){
            Log.d(TAG, "В группе болше 6 студентов. Отмена");
            return false;
        }
        return true;
    }

    public boolean updateViewSubject(Integer idSubject, String newSubjectName){
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            db.execSQL("update SUBJECTVIEW set SUBJECT = \""+newSubjectName+"\" where SUBJECTVIEW._id = "+idSubject+"");
        }
        catch (SQLException e){
            Log.d(TAG, "Не удалось  обновить subjectView");
            return false;
        }
        return true;

    }

    public String execFirstQuery(String dateFrom, String dateTo, String subjectName, Integer groupId){
        String selectQuery = "select idStudent,\n" +
                "\t\tnameStudent,\n" +
                "\t\tSUBJECT,\n" +
                "\t\t(select avg(MARK) from groupsMarks GM1 where GM1.idStudent = GM.idStudent AND SUBJECT = \""+subjectName+"\") [Ср. по предмету],\n" +
                "\t\t(select avg(MARK) from groupsMarks GM2 where GM.idStudent = idStudent AND idGroup = "+groupId.toString()+") [В целом],\n" +
                "\t\t(select avg(MARK) from groupsMarks where idGroup = "+groupId.toString()+") [Ср. для группы]\n" +
                "\t\t\tfrom groupsMarks GM\n" +
                "\t\t\t\twhere idGroup = "+groupId+" AND\n" +
                "\t\t\t\t\t  SUBJECT = \""+subjectName+"\"\n"+
                "GROUP by idStudent";

        String result = new String();
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(DBContract.createViewGroupsMarksQuery);
        Cursor cursor  = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                result += String.format("%s : AVG(Subj)%s : AVG(All)%s : AVG(GROUP)%s\n", cursor.getString(1), cursor.getString(3), cursor.getString(4), cursor.getString(5));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return result;
    }

    public String execSecondQuery(String faculty, String date1, String date2){
        String selectQuery = "select nameStudent, nameFaculty, idGroup, avg(MARK) [avgMark]\n" +
                "\tfrom topFaculty TF\n" +
                "\t\twhere nameFaculty = \n\""+faculty+"\" AND examDate between '"+date1+"' AND '"+date2+"'" +
                "\tGROUP BY idStudent \n" +
                "\tORDER BY avgMark DESC\n" +
                "\tLIMIT 10\n";

        String result = new String();
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(DBContract.createViewTopFacultyQuery);
        Cursor cursor  = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                result += String.format("%s : idGroup -> %s : AVG(mark)%s\n", cursor.getString(0), cursor.getString(2), cursor.getString(3));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return result;
    }


    public String execThirdQuery(String faculty, String date1, String date2){
        String selectQuery = "select nameStudent, idGroup, idFaculty, MARK from worstStudents\n" +
                "\twhere MARK < 4 AND nameFaculty = \""+faculty+"\"\n AND examDate BETWEEN '"+date1+"' AND '"+date2+"'" +
                "\tgroup by idStudent\n" +
                "having MARK < 4;";

        String result = "";
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(DBContract.createViewWorstStudentsQuery);
        Cursor cursor  = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                result += String.format("%s : idGroup -> %s : mark -> %s\n", cursor.getString(0), cursor.getString(1), cursor.getString(3));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return result;
    }

    public String execFourthQuery(String date1, String date2, String subject){
        String createViewQuery = "create temp view if not exists groupsMarks as select\n" +
                "\tstudent._id as idStudent,\n" +
                "\tstudent.name as nameStudent, \n" +
                "\tgroups._id as idGroup, \n" +
                "\tgroups.name as nameGroup,\n" +
                "\tprogress.mark,\n" +
                "\tsubject._id as idSubject, \n" +
                "\tsubject.subject,\n" +
                "\tPROGRESS.EXAMDATE\n" +
                "\t\tfrom groups \n" +
                "\t\t\tinner join student on groups._id = student.idgroup \n" +
                "\t\t\tinner join progress on progress.idstudent = student._id\n" +
                "\t\t\tinner join subject on subject._id = progress.idsubject;\n";

        String selectQuery = "select idGroup,\n" +
                "avg(mark) [Ср. по предмету],\n" +
                "(select avg(MARK) from groupsMarks GM2 where GM2.idGroup = GM.idGroup)\n" +
                "from groupsMarks GM where SUBJECT = \""+subject+"\" AND examDate BETWEEN '"+date1+"' AND '"+date2+"'\n" +
                "group by idGroup;";
        String result = "";
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(createViewQuery);
        Cursor cursor  = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                result += String.format("idGroup -> %s : AVG(mark) -> %s : AVG(ALL) -> %s\n", cursor.getString(0), cursor.getString(1), cursor.getString(2));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return result;

    }

    public String execFifthQuery(String date1, String date2){
        String selectQuery = "select FACULTY.FACULTY, avg(MARK) from PROGRESS\n" +
                "\tinner join STUDENT on PROGRESS.IDSTUDENT = student._id\n" +
                "\tinner join GROUPS on STUDENT.IDGROUP = GROUPS._id\n" +
                "\tinner join FACULTY on GROUPS.FACULTY = FACULTY._id\n" +
                "\twhere PROGRESS.EXAMDATE > '"+date1+"' AND PROGRESS.EXAMDATE < '"+date2+"'\n" +
                "\tgroup by FACULTY.FACULTY\n" +
                "\torder by avg(MARK);";
        String result = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor  = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                result += String.format("%s : mark -> %s\n", cursor.getString(0), cursor.getString(1));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return result;


    }

}
