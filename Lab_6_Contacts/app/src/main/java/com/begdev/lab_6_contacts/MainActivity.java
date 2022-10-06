package com.begdev.lab_6_contacts;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    EditText nameTE, surnameTE, phoneTE;
    DatePicker datePicker;
    Button saveBtn;
    final Context context = this;
    final static String publicFile = "Public_Lab_6.json";
    final static String privateFile = "Private_Lab_6.json";

    ArrayList<Person> arrayPerson = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nameTE = findViewById(R.id.nameTE);
        surnameTE = findViewById(R.id.surnameTE);
        phoneTE = findViewById(R.id.phoneTE);
        datePicker = findViewById(R.id.datePicker);
        saveBtn = findViewById(R.id.backBtn);
        datePicker.setMaxDate(new Date().getTime());

    }


    public void saveBtnClick(View view) throws IOException{
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Тип сохроранения").setMessage("Сохранить публично или приватно?");
        builder.setCancelable(false);

        Person person = new Person(nameTE.getText().toString(),
                surnameTE.getText().toString(), phoneTE.getText().toString(),
                new Date(datePicker.getYear()-1900,datePicker.getMonth(),datePicker.getDayOfMonth()));
        Gson gson = new Gson();

        builder.setPositiveButton("Публично", (dialogInterface, i) -> {
            if (!Utils.ExistBase(publicFile, Utils.FileType.Public, context)) {
                try {
                    Utils.verifyStoragePermissions(this);
                    File f = Utils.CreateCustomFile(publicFile, Utils.FileType.Public, context);
                    FileWriter fw = new FileWriter(f, false);
                    fw.write(gson.toJson(new ArrayList<Person>(Arrays.asList(person))));
                    fw.close();
                    cleanInputUI();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                try {
                    File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), publicFile);
                    BufferedReader bfr = new BufferedReader(new FileReader(f));
                    arrayPerson = gson.fromJson(bfr.readLine(), ArrayList.class);
                    arrayPerson.add(person);
                    FileWriter fw = new FileWriter(f, false);
                    fw.write(gson.toJson(arrayPerson));
                    bfr.close();
                    fw.close();
                    cleanInputUI();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            dialogInterface.cancel();
        });
        builder.setNegativeButton("Приватно", (dialogInterface, i) -> {
            if (!Utils.ExistBase(privateFile, Utils.FileType.Private, context)) {
                try {
                    File f = Utils.CreateCustomFile(privateFile, Utils.FileType.Private, context);
                    FileWriter fw = new FileWriter(f, false);   //rewrite
                    fw.write(gson.toJson(new ArrayList<Person>(Arrays.asList(person))));
                    fw.close();
                    cleanInputUI();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                try{
                    File f = new File(getFilesDir(), privateFile);
                    BufferedReader bfr = new BufferedReader(new FileReader(f));
                    arrayPerson = gson.fromJson(bfr.readLine(), ArrayList.class);
                    arrayPerson.add(person);
                    FileWriter fw = new FileWriter(f, false);
                    fw.write(gson.toJson(arrayPerson));
                    bfr.close();
                    fw.close();
                    cleanInputUI();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            dialogInterface.cancel();
        });
        builder.setNeutralButton("Отмена", (dialogInterface, i) -> {
            dialogInterface.cancel();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void cleanInputUI(){
        this.nameTE.setText("");
        this.surnameTE.setText("");
        this.phoneTE.setText("");
    }
    public void searchBtnClick(View view) {

        Intent searchIntent = new Intent(this, SearchActivity.class);
        startActivity(searchIntent);
    }
}