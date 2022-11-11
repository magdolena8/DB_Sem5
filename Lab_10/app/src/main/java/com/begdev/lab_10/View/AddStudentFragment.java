package com.begdev.lab_10.View;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.begdev.lab_10.Model.DBHelper;
import com.begdev.lab_10.Model.Group;
import com.begdev.lab_10.Model.Student;
import com.begdev.lab_10.R;

public class AddStudentFragment extends Fragment {
    Spinner groupSpinner;
    EditText nameET;
    Button addStudentBtn;
    Group mSelectedGroup;
    ArrayAdapter<Group> mSpinnerAdapter;

    public AddStudentFragment() {
        super(R.layout.fragment_add_student);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        groupSpinner = view.findViewById(R.id.student_group_spinner);
        nameET = view.findViewById(R.id.studentname_ET);
        addStudentBtn = view.findViewById(R.id.btn_addstudent);
        ArrayAdapter<Group> spinnerAdapter = new ArrayAdapter<Group>(getActivity(), android.R.layout.simple_spinner_item, Group.groups);
        groupSpinner.setAdapter(spinnerAdapter);
        groupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mSelectedGroup = (Group) adapterView.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        addStudentBtn.setOnClickListener(onAddStudentListener);
    }

    View.OnClickListener onAddStudentListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String name = nameET.getText().toString();
            int groupId = mSelectedGroup.getId();
            Student student = new Student(groupId, name);
            if (Student.addStudent(student, getActivity())) {
                Toast.makeText(getActivity(), "Добавлено", Toast.LENGTH_SHORT).show();
                nameET.setText("");
            } else {
                Toast.makeText(getActivity(), "Ошибка", Toast.LENGTH_SHORT).show();
            }

        }
    };
}
