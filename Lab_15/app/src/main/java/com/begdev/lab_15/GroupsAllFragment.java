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

public class GroupsAllFragment extends Fragment {
    private Uri groupsUri = Uri.parse("content://com.begdev.provider.university/groups");

    Button btnQuery, btnDelete;
    TextView viewResult;

    public GroupsAllFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groups_all, container, false);
        btnQuery = view.findViewById(R.id.btn_query);
        btnDelete = view.findViewById(R.id.btn_delete);
        viewResult = view.findViewById(R.id.text_view_result);

        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewResult.setText("");
                Cursor cursor = getContext().getContentResolver().query(groupsUri,null,null,null,null);
                while(cursor.moveToNext()){
                    viewResult.append("ID: " + cursor.getString(0) + "\t Head: " + cursor.getString(1) + "\t\tCount: " + cursor.getString(2) + "\n");
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int result=getContext().getContentResolver().delete(groupsUri,null);
                String resultString="Все группы успешно удалены";
                if(result==0){
                    resultString="Нечего удалять";
                }
                Toast.makeText(getContext(),resultString, Toast.LENGTH_SHORT).show();
                viewResult.setText("");
            }
        });

        return view;
    }
}