package com.begdev.lab_6_contacts;

import static com.begdev.lab_6_contacts.MainActivity.publicFile;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchActivity extends AppCompatActivity {
    EditText queryTE;
    TextView resultTW;
    ArrayList<Person> publicArray = new ArrayList<>(), privateArray = new ArrayList<>();

    final static String publicFileName = "Public_Lab_6.json";
    final static String privateFileName = "Private_Lab_6.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        queryTE = findViewById(R.id.queryTE);
        resultTW = findViewById(R.id.resultTW);
        queryTE.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Pattern pattern = Pattern.compile(charSequence.toString().toLowerCase(Locale.ROOT));
                resultTW.setText("");
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
                for (Person person: publicArray) {
                    if(pattern.matcher(person.getName().toLowerCase(Locale.ROOT)).find() || pattern.matcher(person.getSurname().toLowerCase(Locale.ROOT)).find()){
                        resultTW.append("Имя: " + person.getName() + "\r\n");
                        resultTW.append("Фамилия: " + person.getSurname() + "\r\n");
                        resultTW.append("Телефон: " + dateFormat.format(person.getBday()) + "\r\n");
                        resultTW.append("Дата рождения: " + dateFormat.format(person.getBday()) + "\r\n\n");
                    }
                }
                for (Person person: privateArray) {
                    if(pattern.matcher(person.getName().toLowerCase(Locale.ROOT)).find() || pattern.matcher(person.getSurname().toLowerCase(Locale.ROOT)).find()){
                        resultTW.append("Имя: " + person.getName() + "\r\n");
                        resultTW.append("Фамилия: " + person.getSurname() + "\r\n");
                        resultTW.append("Телефон: " + person.getPhone() + "\r\n");
                        resultTW.append("Дата рождения: " + dateFormat.format(person.getBday()) + "\r\n\n");
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
        try {
        Gson gson = new Gson();
        File publicFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), publicFileName);
        File privateFile = new File(super.getFilesDir(), privateFileName);
        BufferedReader bfr = new BufferedReader(new FileReader(publicFile));
        publicArray = gson.fromJson(bfr.readLine(), new TypeToken<ArrayList<Person>>(){}.getType());
        bfr = new BufferedReader(new FileReader(privateFile));
        privateArray = gson.fromJson(bfr.readLine(), new TypeToken<ArrayList<Person>>(){}.getType());
        bfr.close();
    }catch (IOException e){
        e.printStackTrace();
        }
    }
    @Override
    protected void onStart(){
        super.onStart();
        try {
            Gson gson = new Gson();
            File publicFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), publicFileName);
            File privateFile = new File(super.getFilesDir(), privateFileName);
            BufferedReader bfr = new BufferedReader(new FileReader(publicFile));
            this.publicArray = gson.fromJson(bfr.readLine(), new TypeToken<ArrayList<Person>>(){}.getType());
            bfr = new BufferedReader(new FileReader(privateFile));
            privateArray = gson.fromJson(bfr.readLine(), new TypeToken<ArrayList<Person>>(){}.getType());
            bfr.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void backBtnClick(View view){
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }

}
