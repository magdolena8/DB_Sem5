package com.begdev.lab_11;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FifthFragment extends Fragment {
    TextView resultTextView;
    Button findBtn;
    EditText editTimeFrom, editTimeTo;

    public FifthFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_fifth, container, false);
        DBHelper db = new DBHelper(getActivity());
        resultTextView = view.findViewById(R.id.resultTextView);
        editTimeFrom = view.findViewById(R.id.editTimeFrom_second);
        editTimeTo = view.findViewById(R.id.editTimeTo_fourth);

        findBtn = view.findViewById(R.id.findBtn_fourth);

        findBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date1 = editTimeFrom.getText().toString();
                String date2 = editTimeTo.getText().toString();
                resultTextView.setText(db.execFifthQuery(date1, date2));

            }
        });


        return view;
    }
}