package com.begdev.lab_10.View;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import com.begdev.lab_10.Model.Group;
import com.begdev.lab_10.Model.Student;
import com.begdev.lab_10.R;

import java.util.ArrayList;

public class StudentsListFragment extends Fragment {
    Group mDisplayedGroup;
    private ArrayList<Student> studentsArrayList;
    ListView studentsListview;
    ArrayAdapter<Student> adapter;


    public StudentsListFragment(Group group){
        super(R.layout.fragment_studentslist);
        mDisplayedGroup = group;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        studentsArrayList = mDisplayedGroup.getStudentList(getActivity());
        studentsListview = view.findViewById(R.id.students_listview);
        adapter = new ArrayAdapter<Student>(getActivity(), android.R.layout.simple_spinner_item, studentsArrayList);
        studentsListview.setAdapter(adapter);
        studentsListview.setOnItemLongClickListener(studentLongClickListener);

    }

    AdapterView.OnItemLongClickListener studentLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
            showPopupMenu(view, mDisplayedGroup, (Student)adapterView.getItemAtPosition(i));
            return true;
        }
    };

    private void showPopupMenu(View v, Group group, Student student) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), v);
        popupMenu.inflate(R.menu.sethead_popup_menu);

        popupMenu
                .setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.sthead_menu_btn: {
                                group.setHead(getActivity(), student);
                                studentsArrayList = mDisplayedGroup.getStudentList(getActivity());
                                adapter = new ArrayAdapter<Student>(getActivity(), android.R.layout.simple_spinner_item, studentsArrayList);
                                studentsListview.setAdapter(adapter);
//                                adapter.notifyDataSetChanged();
                                return true;
                            }
                            default:
                                return false;
                        }
                    }
                });
        popupMenu.show();
    }

}
