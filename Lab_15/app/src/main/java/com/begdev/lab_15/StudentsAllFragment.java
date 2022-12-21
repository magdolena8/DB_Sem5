package com.begdev.lab_15;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URI;

public class StudentsAllFragment extends Fragment {
    EditText etIdDeleteStudents, etIdGetStudents;
    Button btnGetAllStudents, btnDeleteAllStudents;
    TextView twResultGetStudents;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_students_all, container, false);
        etIdDeleteStudents = view.findViewById(R.id.etIdDeleteStudents);
        etIdGetStudents = view.findViewById(R.id.etIdGetStudents);
        twResultGetStudents = view.findViewById(R.id.twResultGetStudents);
        btnGetAllStudents = view.findViewById(R.id.btnGetAllStudents);
        btnGetAllStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                twResultGetStudents.setText("");
                Uri groupsUri = Uri.parse("content://com.begdev.provider.university/student/" + etIdGetStudents.getText().toString());
                Cursor cursor=getContext().getContentResolver().query(groupsUri,null,null,null,null);
                while(cursor.moveToNext()){
                    twResultGetStudents.append(cursor.getString(0) + " " + cursor.getString(1)+ "\n");
                }
            }
        });

        btnDeleteAllStudents = view.findViewById(R.id.btnDeleteAllStudents);
        btnDeleteAllStudents.setOnClickListener(view1 -> {
            Uri groupsUri = Uri.parse("content://com.begdev.provider.university/student/" + etIdDeleteStudents.getText().toString());
            int result = getContext().getContentResolver().delete(groupsUri,null);
            if(result == 0){
                Toast.makeText(getContext(), "Ошибка при удалении!", Toast.LENGTH_SHORT).show();
            }
            else Toast.makeText(getContext(), "Удалено", Toast.LENGTH_SHORT).show();
        });


        return view;
    }
}