package com.begdev.lab_8;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    ArrayList<Note> noteArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getFragmentManager().beginTransaction().replace(R.id.container_main, new CategoryFragment()).addToBackStack(null).commit();
        try {
            Utils.createFileIfNotExists(getApplicationContext(), "base.xml");
            Note.deserializeNotes(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        BottomNavigationView bottomNavView = findViewById(R.id.bottomNavigationView);
        bottomNavView.setOnItemSelectedListener(navListener);


    }

    private final BottomNavigationView.OnItemSelectedListener navListener = item -> {
//        if (itemId == R.id.add_menu_item) {
//            selectedFragment = new CategoryFragment();
//        } else if (itemId == R.id.categories_menu_item) {
//            selectedFragment = new CategoryFragment();
//        } else if (itemId == R.id.xslt_menu_item) {
//            selectedFragment = new AddFragment();
//        } else if (itemId == R.id.search_menu_item) {
//            selectedFragment = new AddFragment();
//        }

        Fragment selectedFragment = null;
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.add_menu_item: {
                selectedFragment = new AddFragment();
                break;
            }
            case R.id.categories_menu_item: {
                selectedFragment = new CategoryFragment();
                break;
            }
            case R.id.xslt_menu_item: {
                selectedFragment = new XSLTFragment();
                break;
            }
            case R.id.search_menu_item: {
                selectedFragment = new SearchFragment();
                break;
            }
        }

        if (selectedFragment != null) {
            getFragmentManager().beginTransaction().replace(R.id.container_main, selectedFragment).addToBackStack(null).commit();
        }
        return true;
    };


}