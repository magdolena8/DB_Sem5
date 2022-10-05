package com.begdev.lab_6_contacts;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.IOException;

public class Utils {
    public static boolean ExistBase(String fname, FileType fileType, Context context) {
        boolean rc = false;
        File f = null;
        switch (fileType){
            case Private:{
                f = new File(context.getFilesDir(), fname);
                break;
//                File f = new File(context.getFilesDir(), fname);
            }
            case Public:{
                f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), fname);
                break;
            }
        }
        if (rc = f.exists()) Log.d("Log_05", "Файл " + fname + " существует");
        else Log.d("Log_6", "Файл" + fname + " не найден");
        return rc;
    }
    public static File CreateCustomFile(String fname ,FileType fileType, Context context) throws IOException{
        switch (fileType){
            case Public:{
                File documentsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                File f = new File(documentsDir, fname);
                try {
                    f.createNewFile();
                    return f;
                }
                catch (Exception e){
                    Log.d("Lab_6", "PublicFileCreateError" + e.getMessage());
                }
            }
            case Private:{
                File f = new File(context.getFilesDir(), fname);
                try{
                    f.createNewFile();
                    return f;
                }catch(Exception e){
                    Log.d("Lab_6", "PrivateFileCreateError" + e.getMessage());
                }
            }
        }
        return null;
    }
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.MANAGE_EXTERNAL_STORAGE
    };
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
    enum FileType{
        Public,
        Private
    }

}
