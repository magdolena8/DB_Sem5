package com.begdev.lab_11;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DBHelper dbHelper = new DBHelper(this);
        dbHelper.getWritableDatabase().getVersion();

        BottomNavigationView bottomNavView = findViewById(R.id.bottom_nav_bar);
        bottomNavView.setOnItemSelectedListener(navListener);
    }


    private final BottomNavigationView.OnItemSelectedListener navListener = item -> {
        Fragment selectedFragment = null;
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.menu_item1: {
                selectedFragment = new FirstFragment(this);
                break;
            }
            case R.id.menu_item2: {
                selectedFragment = new SecondFragment();
                break;
            }
            case R.id.menu_item3: {
                selectedFragment = new ThirdFragment();
                break;
            }
            case R.id.menu_item4: {
                selectedFragment = new FourthFragment();
                break;
            }
//            case R.id.menu_item5: {
//                selectedFragment = new FifthFragment();
//                break;
//            }
            case R.id.menu_item6: {
                selectedFragment = new SixFragment();
                break;
            }
        }

        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container_fragments_main, selectedFragment).addToBackStack(null).commit();
        }
        return true;
    };
}