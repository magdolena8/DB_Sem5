package com.begdev.lab_16;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavView = findViewById(R.id.nav_view_bottom);
        bottomNavView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();
            switch (itemId) {
                case R.id.menu_item1: {
                    selectedFragment = new ContactsFragment();
                    break;
                }
                case R.id.menu_item2: {
                    selectedFragment = new AddContactFragment();
                    break;
                }
            }
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container_main, selectedFragment).addToBackStack(null).commit();
            }
            return true;
        });



        getSupportFragmentManager().beginTransaction().replace(R.id.container_main, new ContactsFragment()).commit();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                READ_CONTACTS_GRANTED = true;
            }
        }
//        if(READ_CONTACTS_GRANTED){
//            loadContacts();
//        }
//        else{
//            Toast.makeText(this, "Требуется установить разрешения", Toast.LENGTH_LONG).show();
//        }
    }

}