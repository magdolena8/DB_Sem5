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

public class ThirdFragment extends Fragment {
    TextView resultTextView;
    Spinner facultySpinner;
    Button findBtn;
    EditText editTextTimeFrom, editTextTimeTo;
    public ThirdFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_third, container, false);
        DBHelper db = new DBHelper(getContext());
        resultTextView = view.findViewById(R.id.resultTextView);
        facultySpinner = view.findViewById(R.id.facultySpinner);
        findBtn = view.findViewById(R.id.findBtn_third);
        editTextTimeFrom = view.findViewById(R.id.editTextTimeFrom_third);
        editTextTimeTo = view.findViewById(R.id.editTextTimeTo_third);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, db.getFacultiesList());
        facultySpinner.setAdapter(arrayAdapter);

        findBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultTextView.setText(db.execThirdQuery(facultySpinner.getSelectedItem().toString(), editTextTimeFrom.getText().toString(), editTextTimeTo.getText().toString()));
            }
        });

        return view;
    }
}