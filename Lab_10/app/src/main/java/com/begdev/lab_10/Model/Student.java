package com.begdev.lab_10.Model;

import android.content.Context;

import java.util.ArrayList;

public class Student {
    public static ArrayList<Student> students = new ArrayList<>();

    private Integer _id;
    private int _groupId;
    private String _name;
    public boolean isHead = false;

    public Student(int id, int groupId, String name){
        this._id = id;
        this._groupId = groupId;
        this._name = name;
    }
    public Student(int groupId, String name){
        this._groupId = groupId;
        this._name = name;
    }


    public int getId(){
        return this._id;
    }
    public void setId(int id){
        this._id = id;
    }

    public int getGroupId(){
        return this._groupId;
    }
    public void setGroupId(int groupId){
        this._groupId = groupId;
    }

    public String getName(){
        return this._name;
    }
    public void setName(String name){
        this._name = name;
    }

    public static boolean addStudent(Student student, Context context){
        DBHelper dbHelper = new DBHelper(context);
        long insertedRowId = dbHelper.addStudent(student);
        if(insertedRowId != -1){
            student.setId((int) insertedRowId);
            students.add(student);
            return true;
        }
        return false;
    }

    @Override
    public String toString(){
        return this._name + (this.isHead ?"\t-> староста":"");
    }
}
