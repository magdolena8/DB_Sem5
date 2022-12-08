package com.begdev.lab_11;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class SixFragment extends Fragment {
    Spinner deleteGroupSpinner, deleteStudentGroup, addGroupSpinner;
    EditText editNameAdd, editIdgroupUpdate, editNameUpdate;
    Button btnDelete, btnAdd, btnUpdate;

    ArrayAdapter<String> deleteStudentGroupAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        DBHelper dbHelper = new DBHelper(getActivity());

//        ArrayAdapter<Integer> groupSpinnerAdapter = new ArrayAdapter<Integer>(getActivity(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, dbHelper.getGroupsList());
//        deleteGroupSpinner.setAdapter(groupSpinnerAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_six, container, false);
        DBHelper dbHelper = new DBHelper(getActivity());


        deleteGroupSpinner = view.findViewById(R.id.delete_group_spinner);
        deleteStudentGroup = view.findViewById(R.id.delete_student_group);
        addGroupSpinner = view.findViewById(R.id.add_group_spinner);

        editNameAdd = view.findViewById(R.id.edit_name_add);
        editIdgroupUpdate = view.findViewById(R.id.edit_idgroup_update);
        editNameUpdate = view.findViewById(R.id.edit_name_update);

        btnDelete = view.findViewById(R.id.btn_delete);
        btnAdd = view.findViewById(R.id.btn_add);
        btnUpdate = view.findViewById(R.id.btn_update);

        ArrayAdapter<Integer> groupsSpinnerAdapter = new ArrayAdapter<Integer>(getActivity(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, dbHelper.getGroupsList());
        deleteGroupSpinner.setAdapter(groupsSpinnerAdapter);
        addGroupSpinner.setAdapter(groupsSpinnerAdapter);

        deleteGroupSpinner.setAdapter(new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, dbHelper.getGroupsList()));
        deleteGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                deleteStudentGroupAdapter = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_spinner_item,
                        dbHelper.getGroupStudents(Integer.valueOf(adapterView.getItemAtPosition(i).toString())));
                deleteStudentGroup.setAdapter(deleteStudentGroupAdapter);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!dbHelper.deleteStudent(Integer.valueOf(deleteStudentGroup.getSelectedItem().toString().split(" ")[0])))
                {
                    Toast.makeText(getActivity(), "В групее меньше 3 студентов !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer idGroup = Integer.valueOf(addGroupSpinner.getSelectedItem().toString().split(" ")[0]);
                String nameStudent = editNameAdd.getText().toString();
                if(!dbHelper.addStudent(idGroup, nameStudent)){
                    Toast.makeText(getActivity(), "В группе уже больше 6 студентов", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer idSubject = Integer.valueOf(addGroupSpinner.getSelectedItem().toString().split(" ")[0]);
                String newSubjectName = editNameUpdate.getText().toString();
                if(!dbHelper.updateViewSubject(idSubject, newSubjectName)){
                    Toast.makeText(getActivity(), "Не удалось обновить subjectView", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getActivity(), "Обновлено", Toast.LENGTH_SHORT).show();
                }
            }
        });




        return view;
    }
}