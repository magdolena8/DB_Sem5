package com.begdev.lab_8;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class Utils {
    public static boolean ExistBase(String fname, Context context) {
        boolean rc = false;
        File f = new File(context.getFilesDir(), fname);
        if (rc = f.exists()) Log.d("Log_8", "Файл " + fname + " существует");
        else Log.d("Log_8", "Файл" + fname + " не найден");
        return rc;
    }

    public static void showToast(Context context, String msg) {
        //создаём и отображаем текстовое уведомление
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void createFileIfNotExists(Context context, String fname) throws IOException {
        File f = new File(context.getFilesDir(), fname);
        boolean rc = false;
        if (rc = f.exists()) Log.d("Log_8", "Файл " + fname + " существует");
        else{
            f.createNewFile();
            Log.d("Log_8", "Файл " + fname + " создан");
        }
    }
}
