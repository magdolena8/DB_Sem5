package com.begdev.lab_8;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Category {
    public static ArrayList<Category> categoriesArrayList = new ArrayList<>();
//    private static final AtomicInteger count = new AtomicInteger(0);

    private int _id;
    private String _title;

    public Category(String title) {
        this._title = title;
//        this._id = count.incrementAndGet();
        this._id = categoriesArrayList.size() + 1;
    }
    public Category(){
        this._title = null;
    }

    public String getTitle() {
        return this._title;
    }
    public int getID() {
        return this._id;
    }
    public void setID(int id){
        this._id = id;
    }

    public void setTitile(String str) {
        this._title = str;
    }

    public static boolean addCategory(String categoryTitle) {
        if (categoriesArrayList.size() < 5) {
            categoriesArrayList.add(new Category(categoryTitle));
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return this._title;
    }
    @Override
    public int hashCode() {
        return super.hashCode();
    }
    @Override
    public boolean equals(@Nullable Object obj) {
//        return super.equals(obj);
        Category category = (Category) obj;
        if(this._id == category._id && this._title.equals(category._title)) {
            return true;
        }
        return false;

    }
}
