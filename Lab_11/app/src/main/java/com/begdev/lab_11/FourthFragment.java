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

public class FourthFragment extends Fragment {
    TextView resultTextView;
    Button findBtn;
    EditText editTimeFrom, editTimeTo;
    Spinner subjectSpinner;

    public FourthFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fourth, container, false);
        DBHelper db = new DBHelper(getActivity());
        subjectSpinner = view.findViewById(R.id.subjectSpinner_fourth);
        resultTextView = view.findViewById(R.id.resultTextView);
        findBtn = view.findViewById(R.id.findBtn_fourth);
        editTimeFrom = view.findViewById(R.id.editTimeFrom_second);
        editTimeTo = view.findViewById(R.id.editTimeTo_fourth);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, db.getSubjectsList());
        subjectSpinner.setAdapter(arrayAdapter);

//        editTimeFrom = view.findViewById(R.id.editTimeFrom);
//        editTimeTo = view.findViewById(R.id.editTimeTo);
        findBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultTextView.setText(db.execFourthQuery(editTimeFrom.getText().toString(),
                        editTimeTo.getText().toString(),
                        subjectSpinner.getSelectedItem().toString()));
            }
        });

        return view;
    }
}