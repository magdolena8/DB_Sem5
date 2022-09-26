package com.begdev.lab_4;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


public class MainActivity extends AppCompatActivity {
    public static final String baseFileName = "Base_Lab.txt";
    EditText nameEdit, sNameEdit;
    TextView resultTW;
    String name, surname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nameEdit = findViewById(R.id.editPersonName);
        sNameEdit = findViewById(R.id.editPersonSName);
        resultTW = findViewById(R.id.fileContentTW);
    }

    public void savePersonBtnClick(View view) throws Exception{
        if(!ExistBase(baseFileName)){
            File f = new File(super.getFilesDir(), baseFileName);
            try{
                if(f.createNewFile()) AlertUser();
//                Log.d("Log_02", "Файл" +baseFileName+" создан");
            }
            catch (IOException e){
                Log.d("Log_02", "Файл" +baseFileName+" не создан" + e.getMessage());
            }
        }
        else{
            File f = new File(super.getFilesDir(), baseFileName);
            try{
                FileWriter fw = new FileWriter(f, true);
                BufferedWriter bw = new BufferedWriter(fw);
                name = nameEdit.getText().toString();
                surname = sNameEdit.getText().toString();
                if (name.matches("") && surname.matches(""))
                {
                    fw.close();
                    return;
                }
                WriteLine(name, surname, bw);
                if (!PrintFileContent(baseFileName)) throw new FileNotFoundException();
                clearEdits();
            }
            catch (IOException e){
                Log.d("Log_2", "Файл " + baseFileName+" не открыт" + e.getMessage());
            }
        }

    }

    private void clearEdits() {
        nameEdit.setText("");
        sNameEdit.setText("");
    }

    private boolean ExistBase(String fname){
        boolean rc = false;
        File f = new File(super.getFilesDir(), fname);
        if(rc = f.exists()) Log.d("Log_02", "Файл "+ fname + " существует");
        else Log.d("Log_02", "Файл" +fname+" не найден");
        return rc;
    }

    private void AlertUser(){
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Создается файл "+ baseFileName).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("Log_2", "Создание файла "+ baseFileName);
            }
        });
        AlertDialog ad = b.create();
        ad.show();
    }

    private void WriteLine(String name, String sname, BufferedWriter bw){
        String s = sname + " : "+ name +";"+"\r\n";
        try {
            bw.write(s);
            Log.d("Log_2", "Данные записаны");
            bw.close();
        }
        catch (IOException e){
            Log.d("Log_2", e.getMessage());
        }
    }

    private boolean PrintFileContent(String fname) throws Exception {
        try {
            File f = new File(super.getFilesDir(), fname);
            FileReader fr = new FileReader(f);
            Scanner scan = new Scanner(fr);
            resultTW.setText("");
            while (scan.hasNextLine()){ resultTW.append("\n"+scan.nextLine());}
            fr.close();
        }catch (Exception e){
            Log.d("PrintFile error", e.getMessage());
            return false;
        }
        return true;
    }
}