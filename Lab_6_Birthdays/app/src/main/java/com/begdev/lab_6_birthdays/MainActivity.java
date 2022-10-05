package com.begdev.lab_6_birthdays;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    final static String publicFileName = "Public_Lab_6.json";

    ArrayList<Person> publicArray = new ArrayList<>();
    EditText queryTE;
    TextView resultTW;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultTW = findViewById(R.id.resultTW);
        queryTE = findViewById(R.id.queryTE);
        queryTE.addTextChangedListener(customTextWatcher);

        verifyStoragePermissions(this);
        try {
            verifyStoragePermissions(this);

            Gson gson = new Gson();
            File publicFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), publicFileName);
            BufferedReader bfr = new BufferedReader(new FileReader(publicFile));
            publicArray = gson.fromJson(bfr.readLine(), new TypeToken<ArrayList<Person>>(){}.getType());
            bfr.close();

        }catch (IOException e){
            e.printStackTrace();
        }
    }
    TextWatcher customTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            Pattern pattern = Pattern.compile(charSequence.toString().toLowerCase(Locale.ROOT));
            resultTW.setText("");
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
            for (Person person: publicArray) {
                if(pattern.matcher(dateFormat.format(person.getBday())).find()){
                    resultTW.append("Имя: " + person.getName() + "\r\n");
                    resultTW.append("Фамилия: " + person.getSurname() + "\r\n");
                    resultTW.append("Телефон: " + dateFormat.format(person.getBday()) + "\r\n");
                    resultTW.append("Дата рождения: " + dateFormat.format(person.getBday()) + "\r\n\n");
                }
            }
        }
        @Override
        public void afterTextChanged(Editable editable) {}
    };


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.MANAGE_EXTERNAL_STORAGE
    };
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

}