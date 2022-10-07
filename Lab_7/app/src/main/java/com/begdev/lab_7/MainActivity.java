package com.begdev.lab_7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.CalendarView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    CalendarView calendarView;
    EditText noteTE;
    Button addBtn, editBtn, deleteBtn;
    ArrayList<Note> noteArray = new ArrayList<>();
    Date selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calendarView = findViewById(R.id.calendarView);
        noteTE = findViewById(R.id.noteTextEdit);
        addBtn = findViewById(R.id.addBtn);
        editBtn = findViewById(R.id.editBtn);
        deleteBtn = findViewById(R.id.deleteBtn);
        try { noteArray = Note.deserializeNoteArray(this, "database.json");}
        catch (IOException e){e.printStackTrace();}
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                selectedDate = new Date(i, i1, i2);

                for (Note note : noteArray) {
                    if(selectedDate.compareTo(note.date) == 0){
                        noteTE.setText(note.noteText);
                        addBtn.setVisibility(View.GONE);
                        editBtn.setVisibility(View.VISIBLE);
                        deleteBtn.setVisibility(View.VISIBLE);

                        return;
                    }
                }
                editBtn.setVisibility(View.GONE);
                deleteBtn.setVisibility(View.GONE);
                addBtn.setVisibility(View.VISIBLE);
                noteTE.setText("");
            }
        });



    }

    public void addBtnClick(View view) throws IOException{
        if(!noteTE.getText().equals("")){
            if(noteArray.size() >= 10){
                Toast toast = Toast.makeText(getApplicationContext(),"Не сохранено\n\rМассив переполнен", Toast.LENGTH_SHORT);
                toast.show();
            }
            else{
                noteArray.add(new Note(selectedDate, noteTE.getText().toString()));
                Note.serializeNoteArray(this, noteArray, "database.json");
                Toast toast = Toast.makeText(getApplicationContext(),"Сохранено", Toast.LENGTH_SHORT);
                calendarView.setDate(System.currentTimeMillis());
                editBtn.setVisibility(View.VISIBLE);
                deleteBtn.setVisibility(View.VISIBLE);
                addBtn.setVisibility(View.GONE);
                toast.show();
            }
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),"Введите текст заметки", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void editBtnClick(View view) throws IOException{
        for (Note note : noteArray) {
            if(selectedDate.compareTo(note.date) == 0){
                note.noteText = noteTE.getText().toString();
                Note.serializeNoteArray(this, noteArray, "database.json");
                Toast toast = Toast.makeText(getApplicationContext(),"Изменено", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
        }
    }

    public void deleteBtnClick(View view) throws IOException{
        for (Note note : noteArray) {
            if(selectedDate.compareTo(note.date) == 0){
                noteArray.remove(note);
                Note.serializeNoteArray(this, noteArray, "database.json");
                Toast toast = Toast.makeText(getApplicationContext(),"Удалено", Toast.LENGTH_SHORT);
                toast.show();
                editBtn.setVisibility(View.GONE);
                deleteBtn.setVisibility(View.GONE);
                addBtn.setVisibility(View.VISIBLE);
                noteTE.setText("");

                return;
            }
        }
    }

}