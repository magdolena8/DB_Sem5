package com.begdev.lab_10.View;
//
//import android.app.Fragment;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.begdev.lab_10.Model.DBHelper;
import com.begdev.lab_10.Model.Group;
import com.begdev.lab_10.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
//TODO: указание старосты группы. Toasts и др.

public class MainActivity extends AppCompatActivity {
    private DBHelper dbHelper = new DBHelper(this);
    FrameLayout fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentContainer = findViewById(R.id.fragmentcontainer_main);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentcontainer_main, new AddStudentFragment()).commit();
        BottomNavigationView bottomNavBar = findViewById(R.id.navigationbar_bottom);
        bottomNavBar.setOnItemSelectedListener(navListener);
        dbHelper.getGroups();
    }

    public DBHelper getDbHelper() {
        return this.dbHelper;
    }

    private final BottomNavigationView.OnItemSelectedListener navListener = item -> {
        Fragment selectedFragment = null;
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.menubtn_addstudent: {
                selectedFragment = new AddStudentFragment();
                break;
            }
            case R.id.menubtn_addgroup: {
                selectedFragment = new AddGroupFragment();
                break;
            }
            case R.id.menubtn_list: {
                selectedFragment = new GroupsListFragment();
                break;
            }
        }
        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentcontainer_main, selectedFragment).addToBackStack(null).commit();
        }
        return true;
    };
}
