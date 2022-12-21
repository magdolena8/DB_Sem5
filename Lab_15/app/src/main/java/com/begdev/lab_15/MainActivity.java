package com.begdev.lab_15;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.nav_view_bottom);
        bottomNavigationView.setOnItemSelectedListener(navListener);
    }

    private final BottomNavigationView.OnItemSelectedListener navListener = item -> {
        Fragment selectedFragment = null;
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.menu_item1: {
                selectedFragment = new GroupsAllFragment();
                break;
            }
            case R.id.menu_item2:{
                selectedFragment = new GroupSingleFragment();
                break;
            }
            case R.id.menu_item3:{
                selectedFragment = new StudentsAllFragment();
                break;
            }
            case R.id.menu_item4:{
                selectedFragment = new StudentsSingleFragment();
                break;
            }
        }
        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container_fragments_main, selectedFragment).addToBackStack(null).commit();
            return true;
        }
        return false;
    };
}