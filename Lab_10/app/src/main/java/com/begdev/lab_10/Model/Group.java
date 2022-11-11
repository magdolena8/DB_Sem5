package com.begdev.lab_10.Model;

import android.content.Context;

import java.util.ArrayList;

public class Group {
    public static ArrayList<Group> groups = new ArrayList<>();
    public ArrayList<Student> _groupStudents;

    private Integer _id = null;
    private String _faculty;
    private int _course;
    private String _groupName;
    private int _headId;

    public Group(String faculty, int course, String groupName) {
        this._faculty = faculty;
        this._course = course;
        this._groupName = groupName;
    }

    public Group(int id, String faculty, int course, String groupName, int headId) {
        this._id = id;
        this._faculty = faculty;
        this._course = course;
        this._groupName = groupName;
        this._headId = headId;
    }

    public int getId() {
        return this._id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public String getFaculty() {
        return this._faculty;
    }

    public void setFaculty(String faculty) {
        this._faculty = faculty;
    }

    public int getCourse() {
        return this._course;
    }

    public void setCourse(int course) {
        this._course = course;
    }

    public String getGroupName() {
        return this._groupName;
    }

    public void setGroupName(String groupName) {
        this._groupName = groupName;
    }

    public int getHeadId() {
        return this._headId;
    }

    public static boolean addGroup(Group group, Context context) {
        DBHelper dbHelper = new DBHelper(context);
        long insertedRowId = dbHelper.addGroup(group);
        if(insertedRowId != -1){
            groups.add(group);
            group.setId((int) insertedRowId);
            return true;
        }
        return false;
    }

    public static boolean deleteGroup(Context context, Group group) {
        DBHelper dbHelper = new DBHelper(context);
        int dbResult = dbHelper.deleteGroup(group);
        if (dbResult != -1) {
            groups.remove(group);
            return true;
        }
        return false;
    }

    public ArrayList<Student> getStudentList(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        this._groupStudents = dbHelper.getStudentsByGroup(this);
        return _groupStudents;
    }

    public boolean setHead(Context context, Student student){
        DBHelper dbHelper = new DBHelper(context);
        this._headId = student.getId();
        dbHelper.addGroupHead(this, student);
        return true;
    }

    @Override
    public String toString() {
        return this._course + " " + this._faculty + " " + this._groupName;
    }

}
