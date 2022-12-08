package com.begdev.lab_11;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class FirstFragment extends Fragment {
    Button findBtn;
    EditText timeEditFrom, timeEditTo;
    TextView resultTextView;
    Spinner groupSpinner, subjectSpinner;
    public FirstFragment(Context context) {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DBHelper db = new DBHelper(getContext());
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        findBtn = view.findViewById(R.id.findBtn);
        resultTextView = view.findViewById(R.id.result_tw);
        timeEditFrom = view.findViewById(R.id.editTimeFrom);
        timeEditTo = view.findViewById(R.id.editTimeTo);
        groupSpinner = view.findViewById(R.id.groupSpinner);
        subjectSpinner = view.findViewById(R.id.subjectSpinner);

        ArrayAdapter<Integer> groupSpinnerAdapter = new ArrayAdapter<Integer>(getActivity(), android.R.layout.simple_spinner_item, db.getGroupsList());
        groupSpinner.setAdapter(groupSpinnerAdapter);

        ArrayAdapter<String> subjectSpinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, db.getSubjectsList());
        subjectSpinner.setAdapter(subjectSpinnerAdapter);

        findBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String timeFrom = timeEditFrom.getText().toString();
                String timeTo = timeEditFrom.getText().toString();
                String result = db.execFirstQuery(timeFrom, timeTo, subjectSpinner.getSelectedItem().toString(), (Integer)groupSpinner.getSelectedItem());
                resultTextView.setText(result);
            }
        });


        return view;
    }
    @Override
    public void onStart()    {
        super.onStart();
    }
}