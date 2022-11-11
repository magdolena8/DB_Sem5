package com.begdev.lab_10.Model;

import android.provider.BaseColumns;

public class DBContract {
    public DBContract() {
    }

    public static abstract class DBGroupsTable implements BaseColumns {
        public static final String TABLE_NAME = "GROUPS";
        public static final String COLUMN_NAME_IDGROUP = "_id";
        public static final String COLUMN_NAME_FACULTY = "FACULTY";
        public static final String COLUMN_NAME_COURSE = "COURSE";
        public static final String COLUMN_NAME_NAME = "NAME";
        public static final String COLUMN_NAME_HEAD = "HEAD";
    }

    public static abstract class DBStudentsTable implements BaseColumns {
        public static final String TABLE_NAME = "STUDENTS";
        public static final String COLUMN_NAME_IDSTUDENT = "_id";
        public static final String COLUMN_NAME_IDGROUP = "IDGROUP";
        public static final String COLUMN_NAME_NAME = "NAME";
    }

    public static String DB_NAME = "lab_10.db";
    public static String SQL_CREATE_GROUPS_TABLE = "CREATE TABLE IF NOT EXISTS " + DBGroupsTable.TABLE_NAME + " (" +
            DBGroupsTable.COLUMN_NAME_IDGROUP + " INTEGER PRIMARY KEY AUTOINCREMENT, " + //PK
            DBGroupsTable.COLUMN_NAME_FACULTY + " TEXT NOT NULL CHECK (" + DBGroupsTable.COLUMN_NAME_FACULTY + " != \"\"), " +
            DBGroupsTable.COLUMN_NAME_COURSE + " INTEGER NOT NULL, " +
            DBGroupsTable.COLUMN_NAME_NAME + " TEXT NOT NULL UNIQUE, " +
            DBGroupsTable.COLUMN_NAME_HEAD + " TEXT INTEGER,  " +      //should references to one of student
            "FOREIGN KEY(" + DBGroupsTable.COLUMN_NAME_HEAD + ") REFERENCES " + DBStudentsTable.TABLE_NAME + "(" + DBStudentsTable.COLUMN_NAME_IDSTUDENT + ") " +
            "ON DELETE CASCADE " +
            "ON UPDATE CASCADE)";

    public static String SQL_CREATE_STUDENTS_TABLE = "CREATE TABLE IF NOT EXISTS " + DBStudentsTable.TABLE_NAME + " (" +
            DBStudentsTable.COLUMN_NAME_IDSTUDENT + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            DBStudentsTable.COLUMN_NAME_IDGROUP + " INTEGER, " +   //FK
            DBStudentsTable.COLUMN_NAME_NAME + " TEXT UNIQUE CHECK (" + DBStudentsTable.COLUMN_NAME_NAME + " != \"\"), " +
            "FOREIGN KEY (" + DBStudentsTable.COLUMN_NAME_IDGROUP + ") REFERENCES " + DBGroupsTable.TABLE_NAME + "(" + DBGroupsTable.COLUMN_NAME_IDGROUP + ") " +
            "ON UPDATE CASCADE " +
            "ON DELETE CASCADE)";
}
