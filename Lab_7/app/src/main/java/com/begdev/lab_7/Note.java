package com.begdev.lab_7;

import android.app.Activity;
import android.os.Environment;
import android.telephony.mbms.StreamingServiceInfo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.begdev.lab_7.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class Note {
    Date date;
    String noteText;
    public Note(Date date, String noteText){
        this.date = date;
        this.noteText = noteText;
    }
    @Override
    public String toString(){
        super.toString();
        return this.date.toString() + " " + this.noteText;
    }

    public static ArrayList<Note> deserializeNoteArray(Activity activity, String privateFileName) throws IOException {
        ArrayList<Note> result;
//        if(!Utils.ExistBase("database.json", activity)) {
//
//        }
        Gson gson = new Gson();
        File privateFile = new File(activity.getFilesDir(), privateFileName);
        BufferedReader bfr = new BufferedReader(new FileReader(privateFile));
        result = gson.fromJson(bfr.readLine(), new TypeToken<ArrayList<Note>>(){}.getType());
        bfr.close();
        return result;
    }
    public static boolean serializeNoteArray(Activity activity, ArrayList<Note> arrayNote, String privateFileName) throws IOException{
        File privateFile = new File(activity.getFilesDir(), privateFileName);
        if(!Utils.ExistBase("database.json", activity)){
            privateFile.createNewFile();
        }
        Gson gson = new Gson();
//        File privateFile = new File(activity.getFilesDir(), privateFileName);
        FileWriter fw = new FileWriter(privateFile, false);
        fw.write(gson.toJson(arrayNote));
        fw.close();
        return false;
    }

}
