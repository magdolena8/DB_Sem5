package com.begdev.lab_11;

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

public class SecondFragment extends Fragment {
    Button findBtn;
    TextView resultTextView;
    Spinner facultySpinner;
    EditText editTimeFrom, editTimeTo;
    public SecondFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DBHelper db = new DBHelper(getContext());
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        findBtn = view.findViewById(R.id.findBtn_second);
        resultTextView = view.findViewById(R.id.resultTextView);
        facultySpinner = view.findViewById(R.id.facultySpinner);
        editTimeFrom = view.findViewById(R.id.editTimeFrom_second);
        editTimeTo = view.findViewById(R.id.editTimeTo_second);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, db.getFacultiesList());
        facultySpinner.setAdapter(arrayAdapter);


        findBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result = db.execSecondQuery(facultySpinner.getSelectedItem().toString(),
                        editTimeFrom.getText().toString(),
                        editTimeTo.getText().toString()
                        );
                resultTextView.setText(result);
            }
        });
        return view;
    }
}