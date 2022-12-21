package com.begdev.lab_15;

import android.content.ContentValues;
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

import java.util.List;

public class StudentsSingleFragment extends Fragment {
    EditText etIdGroupGetSingleStudent, etIdStudentGetSingleStudent,etNameStudentAddSingleStudent,etIdGroupStudentAddSingleStudent,
            etBirthdateStudentAddSingleStudent, etAddressStudentAddSingleStudent, etIdStudentDeleteSingleStudent,etIdGroupUpdateSingleStudent,
            etIdStudentUpdateSingleStudent, etNameStudentUpdateSingleStudent, etIdGroupDeleteSingleStudent;
    TextView twResultGetSingleStudent;
    Button btnGetStudentInfo, btnAddStudent, btnDeleteStudent, btnUpdateStudent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_students_single, container, false);
        etIdGroupGetSingleStudent = view.findViewById(R.id.etIdGroupGetSingleStudent);
        etIdStudentGetSingleStudent = view.findViewById(R.id.etIdStudentGetSingleStudent);
        etNameStudentAddSingleStudent = view.findViewById(R.id.etNameStudentAddSingleStudent);
        etIdGroupStudentAddSingleStudent = view.findViewById(R.id.etIdGroupStudentAddSingleStudent);
        etBirthdateStudentAddSingleStudent = view.findViewById(R.id.etBirthdateStudentAddSingleStudent);
        etAddressStudentAddSingleStudent = view.findViewById(R.id.etAddressStudentAddSingleStudent);
        etIdStudentDeleteSingleStudent = view.findViewById(R.id.etIdStudentDeleteSingleStudent);
//        etIdGroupUpdateSingleStudent = view.findViewById(R.id.etIdGroupUpdateSingleStudent);
        etIdStudentUpdateSingleStudent = view.findViewById(R.id.etIdStudentUpdateSingleStudent);
        etNameStudentUpdateSingleStudent = view.findViewById(R.id.etNameStudentUpdateSingleStudent);
        twResultGetSingleStudent = view.findViewById(R.id.twResultGetSingleStudent);
//        etIdGroupDeleteSingleStudent = view.findViewById(R.id.etIdGroupDeleteSingleStudent);

        btnGetStudentInfo = view.findViewById(R.id.btnGetStudentInfo);
        btnGetStudentInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                twResultGetSingleStudent.setText("");
                Uri studentUri=Uri.parse("content://com.begdev.provider.university/student/" + etIdGroupGetSingleStudent.getText().toString() + "/" + etIdStudentGetSingleStudent.getText().toString());
                Cursor cursor = getContext().getContentResolver().query(studentUri,null,null,null,null);
                if(cursor != null){
                    while(cursor.moveToNext()){
                        twResultGetSingleStudent.append("ID: " + cursor.getString(0) + " Имя: " + cursor.getString(1)+ "\n");
                    }
                }
                else Toast.makeText(getContext(), "Студент не наден!", Toast.LENGTH_SHORT).show();
            }
        });

        btnAddStudent = view.findViewById(R.id.btnAddStudent);
        btnAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                values.put("NAME",etNameStudentAddSingleStudent.getText().toString());
                values.put("IDGROUP",etIdGroupStudentAddSingleStudent.getText().toString());
                values.put("BIRTHDATE",etBirthdateStudentAddSingleStudent.getText().toString());
                values.put("ADDRESS",etAddressStudentAddSingleStudent.getText().toString());
                Uri studentUri = Uri.parse("content://com.begdev.provider.university/groups/" + etIdGroupStudentAddSingleStudent.getText().toString());
                 getContext().getContentResolver().insert(studentUri,values,null);

            }
        });



        btnDeleteStudent = view.findViewById(R.id.btnDeleteStudent);
        btnDeleteStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri studentUri=Uri.parse("content://com.begdev.provider.university/student/0/" + etIdStudentDeleteSingleStudent.getText().toString());
                int result = getContext().getContentResolver().delete(studentUri,null);
//                String resultString="Студент успешно удалён";
                if(result == 0){
                    Toast.makeText(getContext(), "Ошибка!", Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(getContext(), "Удалено", Toast.LENGTH_SHORT).show();

            }
        });

        btnUpdateStudent = view.findViewById(R.id.btnUpdateStudent);
        btnUpdateStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                values.put("NAME", etNameStudentUpdateSingleStudent.getText().toString());
                Uri studentUri=Uri.parse("content://com.begdev.provider.university/student/" + etIdStudentUpdateSingleStudent.getText().toString());
                int result = getContext().getContentResolver().update(studentUri, values, null);
                if(result == 0){
                    Toast.makeText(getContext(), "Ошибка!", Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(getContext(), "Изменено", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}