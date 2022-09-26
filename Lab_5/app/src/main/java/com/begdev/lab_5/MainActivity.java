package com.begdev.lab_5;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

public class MainActivity extends AppCompatActivity {
    String tableFileName = "Lab_5.txt";
    EditText inputKeyEdit, inputValueEdit, outputKeyEdit;
    TextView outputValueTW;
    int rowLength = 21;
    String emptyRowTemplate = "     :          :  ;";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputKeyEdit = findViewById(R.id.addKeyEdit);
        inputValueEdit = findViewById(R.id.addValueEdit);
        outputKeyEdit = findViewById(R.id.outputKeyEdit);
        outputValueTW = findViewById(R.id.resultValueTW);

        if(!ExistBase(tableFileName)){
            File f = new File(super.getFilesDir(), tableFileName);
            try{
                f.createNewFile();
                writeTableTemplate(tableFileName);
            }
            catch(Exception error){
                Log.d("Log_5", "Create file error");
            }
        }
    }

    public void addNewValueBtnClick(View view){
        try{
            String key = inputKeyEdit.getText().toString();
            String value = inputValueEdit.getText().toString();
            Toast toast;
            if(!addValue(key, value)){
                toast = Toast.makeText(this, "Ошибка при добавлении",Toast.LENGTH_SHORT);
            }
            else toast = Toast.makeText(this, "Успешно добавлено", Toast.LENGTH_SHORT);
            toast.show();
        }
        catch (Exception error){
            Log.d("Lab_5", "addNewValueBtnClickError "+error.getMessage());
        }
    }
    public void getValueBtnClick(View view) throws IOException {
        String key = outputKeyEdit.getText().toString();
        outputValueTW.setText(getValue(key));
    }
    private String getValue(String key) throws IOException{
        File f = new File(getFilesDir(), tableFileName);
        RandomAccessFile rafile = new RandomAccessFile(f, "r");
        long rowPtr = getRowPtr(key);
        String line;
        while (true){
            rafile.seek(rowPtr);
            line = rafile.readLine();
            if(key.matches(extractKeyFromString(line))){
                return extractValueFromString(line);
            }
            if(extractRowFromString(line) == -1){
                return "ключ не найден";
            }
            rowPtr = getRowPtr(extractRowFromString(line));
        }
    }
    private boolean addValue(String key, String value) throws IOException{
        String resultStr = buildString(decorateKeyValue(key, 0), decorateKeyValue(value, 1));        //АГОООООООНЬ (убейте меня)
        long rowStartPtr;
        try {
            File f = new File(getFilesDir(), tableFileName);
            RandomAccessFile raFile = new RandomAccessFile(f, "rw");
            rowStartPtr = getRowPtr(key);
            raFile.seek(rowStartPtr);
            String line = raFile.readLine();

            if(line.matches(emptyRowTemplate)){
                raFile.seek(rowStartPtr);
                raFile.writeBytes(resultStr);
                Log.d("Lab_5", "Записали в заранее размеченную строку");
            }
            else{
                long nextPtr = rowStartPtr;
                while(true)
                {
                    if(extractKeyFromString(line).matches(key)){    //переписать значение
                        raFile.seek(nextPtr + 6);
                        Log.d("Lab_5", raFile.readLine());
                        raFile.seek(nextPtr + 6);
                        raFile.writeBytes(value);
                        break;
                    }
                    if(rowNumToPtr(extractRowFromString(line)) > 0) {
                        nextPtr = rowNumToPtr(extractRowFromString(line));
                        raFile.seek(nextPtr);
                        line = raFile.readLine();
                    }
                    else{
                        raFile.seek(nextPtr + 17);
                        raFile.writeBytes(Long.toString(raFile.length() / rowLength));
                        raFile.seek(raFile.length());
                        raFile.writeBytes(resultStr);
                        break;
                    }
                }
            }
            raFile.close();
        }
        catch (Exception error){
            Log.d("Lab_5", "addValueError " + error);
            return false;
        }
        return true;
    }

    private int extractRowFromString(String str){
        try{
            String a = str.substring(str.lastIndexOf(':')+1, str.lastIndexOf(':')+3);
            String b = a.replaceAll("\\s", "");
            int i = Integer.parseInt(b);
            return i;
        }
        catch (Exception e){
            return -1;
        }
    }
    private String extractKeyFromString(String str){
            String a = str.substring(0,5);
            String b = a.replaceAll("\\s", "");
            return b;
    }
    private String extractValueFromString(String str){
        String a = str.substring(6,16);
        String b = a.replaceAll("\\s", "");
        return b;
    }
    private long rowNumToPtr(int rowNum){
        return rowNum * rowLength;
    }

    private int getRowPtr(int i){
        return i*rowLength;
    }
    private int getRowPtr(String key){
        Log.d("Lab_5","Посчитали хэш(позицию): "+(key.hashCode()%10*rowLength));
        return key.hashCode()%10*rowLength;
    }
    private String buildString(String key, String value){
        StringBuffer strbuff = new StringBuffer(key);
        strbuff.append(":").append(value).append(":").append("  ").append(";\n");
        return strbuff.toString();
    }
    private String buildString(String key, String value, int ptr){
        StringBuffer strbuff = new StringBuffer(key);
        strbuff.append(":").append(value).append(":").append(ptr).append("\n");
        return strbuff.toString();
    }
    private String decorateKeyValue(String value, int type){
        String repeatedSpaces = new String();
        int valueLength = value.length();
        StringBuffer decoratedString = new StringBuffer(value.toString());
        if(type == 0){
            repeatedSpaces = new String(new char[5 - value.length()]).replace("\0", " ");  //дописатаь недостающие пробелы
        }
        else if(type == 1){
            repeatedSpaces = new String(new char[10 - decoratedString.length()]).replace("\0", " ");  //дописатаь недостающие пробелы
        }
        else{
            Log.d("Lab_5",  "BuildString Error" + "Неверный параметр декорации сроик");
            return null;
        }
        decoratedString.append(repeatedSpaces);
        Log.d("Lab_5", "Сдекорировали:  _" + decoratedString + "_");
        return decoratedString.toString();
    }
    private boolean ExistBase(String fname){
        boolean rc = false;
        File f = new File(super.getFilesDir(), fname);
        if(rc = f.exists()) Log.d("Log_05", "Файл "+ fname + " существует");
        else Log.d("Log_05", "Файл" +fname+" не найден");
        return rc;
    }
    private boolean writeTableTemplate(String fname) throws IOException{
        {
            String stringTemplate = "     :          :  ;\n"; //5:10:2;
            File f = new File(super.getFilesDir(), fname);
            FileWriter fw = new FileWriter(f, true);
            for (int i = 0; i < 10; i++) {
                fw.write(stringTemplate);
            }
            fw.close();
        }
        return true;
    }

}