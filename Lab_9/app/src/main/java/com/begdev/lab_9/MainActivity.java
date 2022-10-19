package com.begdev.lab_9;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    EditText idTE, fTE, tTE;
    Button insertBtn, selectBtn, selectRowBtn, updateBtn, deleteBtn;
    String DBFilename = "Lab_DB.db";
    CustomDBHelper dbHelper = new CustomDBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        idTE = findViewById(R.id.idTE);
        fTE = findViewById(R.id.fTE);
        tTE = findViewById(R.id.tTE);
        insertBtn = findViewById(R.id.insertBtn);
        selectBtn = findViewById(R.id.selectBtn);
        selectRowBtn = findViewById(R.id.selectRowBtn);
        updateBtn = findViewById(R.id.updateBtn);
        deleteBtn = findViewById(R.id.deleteBtn);

        if (!ExistBase(DBFilename, this)) {
            try {
                File f = new File(getFilesDir(), DBFilename);
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void onInsertBtnClick(View view) throws NumberFormatException, NullPointerException {
        Integer i = idTE.getText().toString().matches("") ?
                null :
                Integer.parseInt(String.valueOf(idTE.getText()));
        Float f = fTE.getText().toString().matches("") ?
                null :
                Float.parseFloat(String.valueOf(fTE.getText()));
        String t = tTE.getText().toString().matches("") ?
                null :
                tTE.getText().toString();


        if (dbHelper.insertRow(i, f, t)) {
            Toast toast = Toast.makeText(this, "Добавлено", Toast.LENGTH_LONG);
            toast.show();
            tTE.setText("");
            idTE.setText("");
            fTE.setText("");
        } else {
            Toast toast = Toast.makeText(this, "Ошибка ввода", Toast.LENGTH_LONG);
            toast.show();
        }

    }

    public void onSelectBtnClick(View view){
        DBSet result = dbHelper.select(Integer.parseInt(String.valueOf(idTE.getText())));
        if(result != null){
            tTE.setText(result.T);
            fTE.setText(result.F.toString());
            Toast toast = Toast.makeText(this, "Найдено", Toast.LENGTH_LONG);
            toast.show();
        }
        else{
            tTE.setText("");
            idTE.setText("");
            fTE.setText("");
            Toast toast = Toast.makeText(this, "Строка не найдна", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void onSelectRowBtnClick(View view){

        DBSet result = dbHelper.selectRaw(Integer.parseInt(String.valueOf(idTE.getText())));
        if(result != null){
            tTE.setText(result.T);
            tTE.setText(result.F.toString());
            Toast toast = Toast.makeText(this, "Найдено", Toast.LENGTH_LONG);
            toast.show();
        }
        else{
            tTE.setText("");
            idTE.setText("");
            fTE.setText("");
            Toast toast = Toast.makeText(this, "Строка не найдна", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void onUpdateBtnCLick(View view){
        if(!dbHelper.update(Integer.parseInt(String.valueOf(idTE.getText())),
                Float.parseFloat(String.valueOf(fTE.getText())),
                tTE.getText().toString())){
            Toast toast = Toast.makeText(this, "Ошибка ввода", Toast.LENGTH_LONG);
            toast.show();
        }
        else{
            Toast toast = Toast.makeText(this, "Значения обновлены", Toast.LENGTH_LONG);
            toast.show();

        }
    }
    public void onDeleteBtnClick(View view){
        if(!dbHelper.delete(Integer.parseInt(String.valueOf(idTE.getText())))){
            Toast toast = Toast.makeText(this, "Ошибка ввода", Toast.LENGTH_LONG);
            toast.show();
        }
        else{
            Toast toast = Toast.makeText(this, "Удалено", Toast.LENGTH_LONG);
            toast.show();
            tTE.setText("");
            idTE.setText("");
            fTE.setText("");
        }
    }

    public static boolean ExistBase(String fname, Context context) {
        boolean rc = false;
        File f = new File(context.getFilesDir(), fname);

        if (rc = f.exists()) Log.d("Log_05", "Файл " + fname + " существует");
        else Log.d("Log_6", "Файл" + fname + " не найден");
        return rc;
    }
}