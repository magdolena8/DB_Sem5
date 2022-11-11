package com.begdev.lab_10.View;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.begdev.lab_10.Model.Group;
import com.begdev.lab_10.R;

public class AddGroupFragment extends Fragment {
    Button addGroupBtn;
    EditText facultyET, courseET, groupNameET;

    public AddGroupFragment() {
        super(R.layout.fragment_add_group);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        addGroupBtn = view.findViewById(R.id.btn_addstudent);
        addGroupBtn.setOnClickListener(onAddClickListener);
        facultyET = view.findViewById(R.id.faculty_ET);
        groupNameET = view.findViewById(R.id.groupname_ET);
        courseET = view.findViewById(R.id.course_ET);
    }

    View.OnClickListener onAddClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String faculty = String.valueOf(facultyET.getText());
            int course = Integer.parseInt(courseET.getText().toString());
            String groupName = String.valueOf(groupNameET.getText());
            Group group = new Group(faculty, course, groupName);
            if (Group.addGroup(group, getActivity())) {
                Toast.makeText(getActivity(), "Добавлено", Toast.LENGTH_SHORT).show();
                facultyET.setText("");
                groupNameET.setText("");
                courseET.setText("");
            } else {
                Toast.makeText(getActivity(), "Ошибка", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
