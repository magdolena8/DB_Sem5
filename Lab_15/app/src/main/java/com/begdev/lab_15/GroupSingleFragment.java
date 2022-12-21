package com.begdev.lab_15;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
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


public class GroupSingleFragment extends Fragment {
//    Uri groupsUri=Uri.parse("content://com.demo.provider/GROUPS/" + groupID.getText().toString());


    Button btnGetGroupInfo, btnAddGroup, btnDeleteGroup,btnUpdateGroup;
    EditText etIdGetGroup, etFacultyAddGroup, etCourseAddGroup, etNameAddGroup,
            etIdDeleteGroup, etIdUpdateGroup, etNameUpdateGroup, etHeadUpdateGroup;
    TextView twInfoResult;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_single, container, false);
        etIdGetGroup = view.findViewById(R.id.etIdGetGroup);
        twInfoResult = view.findViewById(R.id.twInfoResult);
        btnGetGroupInfo = view.findViewById(R.id.btnGetGroupInfo);
        btnGetGroupInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                twInfoResult.setText("");
                Uri groupsUri = Uri.parse("content://com.begdev.provider.university/groups/" + etIdGetGroup.getText().toString());
                Cursor cursor = getContext().getContentResolver().query(groupsUri,null,null,null,null);
                while(cursor.moveToNext()){
                    twInfoResult.append("Староста: " + cursor.getString(0) + "\nКоличество студентов: " + cursor.getString(1)+ "\n");
                }
            }
        });
        btnAddGroup = view.findViewById(R.id.btnAddGroup);
        etFacultyAddGroup = view.findViewById(R.id.etFacultyAddGroup);
        etCourseAddGroup = view.findViewById(R.id.etCourseAddGroup);
        etNameAddGroup = view.findViewById(R.id.etNameAddGroup);
        btnAddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                values.put("FACULTY", etFacultyAddGroup.getText().toString());
                values.put("COURSE", etCourseAddGroup.getText().toString());
                values.put("NAME", etNameAddGroup.getText().toString());
                Uri groupUri=Uri.parse("content://com.begdev.provider.university/groups");
                getContext().getContentResolver().insert(groupUri,values,null);
            }
        });

        etIdDeleteGroup = view.findViewById(R.id.etIdDeleteGroup);
        btnDeleteGroup = view.findViewById(R.id.btnDeleteGroup);
        btnDeleteGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri groupsUri=Uri.parse("content://com.begdev.provider.university/groups/" + etIdDeleteGroup.getText().toString());
                int result = getContext().getContentResolver().delete(groupsUri,null);
                getContext().getContentResolver().delete(groupsUri,null);
                if(result == 0){
                    Toast.makeText(getContext(), "Ошибка удадения", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnUpdateGroup = view.findViewById(R.id.btnUpdateGroup);
        etIdUpdateGroup = view.findViewById(R.id.etIdUpdateGroup);
        etNameUpdateGroup = view.findViewById(R.id.etNameUpdateGroup);
        etHeadUpdateGroup = view.findViewById(R.id.etHeadUpdateGroup);

        btnUpdateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                if(!etNameUpdateGroup.getText().toString().equals("")) {
                    values.put("NAME", etNameUpdateGroup.getText().toString());
                }
                if(!etHeadUpdateGroup.getText().toString().equals("")) {
                    values.put("HEAD", etHeadUpdateGroup.getText().toString());
                }
                Uri studentUri=Uri.parse("content://com.begdev.provider.university/groups/" + etIdUpdateGroup.getText().toString());
                try {
                    int result = getContext().getContentResolver().update(studentUri, values, null);
                }catch (SQLiteException e){e.printStackTrace();
                    Toast.makeText(getContext(), "Ошибка внешнего ключа!", Toast.LENGTH_SHORT).show();
                }
            }
        });






        return view;
    }
}