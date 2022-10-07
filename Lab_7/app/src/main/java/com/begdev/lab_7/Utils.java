package com.begdev.lab_7;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;

public class Utils {
    public static boolean ExistBase(String fname, Context context) {
        boolean rc = false;
        File f = new File(context.getFilesDir(), fname);
        if (rc = f.exists()) Log.d("Log_07", "Файл " + fname + " существует");
        else Log.d("Log_07", "Файл" + fname + " не найден");
        return rc;
    }
}
