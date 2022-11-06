package com.begdev.lab_8;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;    //read about


import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;

public class AddFragment extends Fragment {
    CalendarView calendarView;
    EditText noteTE;
    Spinner categorySpinner;
    Button addBtn, editBtn, deleteBtn, applyEditBtn;
    Date mSelectedDate;
    Note mSelectedNote;
    ListView notesListView;
    ArrayAdapter<Note> mListAdapter;
    ArrayAdapter<Category> spinnerAdapter;


    private Category mCategory;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        noteTE = view.findViewById(R.id.noteTE);
        editBtn = view.findViewById(R.id.editBtn);
        deleteBtn = view.findViewById(R.id.deleteBtn);
        applyEditBtn = view.findViewById(R.id.applyEditBtn);
        notesListView = view.findViewById(R.id.notesListView);
//        mListAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, Note.notesArrayList);
        try {
            Note.deserializeNotes(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        notesListView.setAdapter(mListAdapter);
        notesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mSelectedNote = (Note) adapterView.getItemAtPosition(i);
                editBtn.setVisibility(View.VISIBLE);
                addBtn.setVisibility(View.GONE);
//                adapterView.notifyAll();
                for (int item = 0; item < mListAdapter.getCount(); item++) {
                    View v = notesListView.getChildAt(item);
                    v.setBackgroundColor(getResources().getColor(R.color.teal_200));
                }
                view.setBackgroundColor(Color.GREEN);
                noteTE.setText(mSelectedNote.text);
                categorySpinner.setSelection(spinnerAdapter.getPosition(mSelectedNote.category));
            }
        });

        categorySpinner = view.findViewById(R.id.categorySpinner);
        spinnerAdapter = new ArrayAdapter<Category>(getActivity(), android.R.layout.simple_spinner_item, Category.categoriesArrayList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(spinnerAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mCategory = (Category) adapterView.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        calendarView = view.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                mSelectedDate = new Date(i, i1, i2); //TODO: i - 1900
                ArrayList<Note> topicalNotes = (ArrayList<Note>) Note.notesArrayList.stream().filter(c -> c.date.compareTo(mSelectedDate) == 0).collect(Collectors.toCollection(ArrayList<Note>::new));
                mListAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, topicalNotes);
                notesListView.setAdapter(mListAdapter);
            }
        });
        addBtn = view.findViewById(R.id.addBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Note note = new Note(selectedDate, noteTE.getText().toString());
                noteTE.setVisibility(View.VISIBLE);
                categorySpinner.setVisibility(View.VISIBLE);
                categorySpinner.setSelection(0, true);
                Note note = noteTE.getText().toString().matches("") ?
                        null :
                        new Note(mSelectedDate, noteTE.getText().toString(), mCategory);
                if (note != null) {
                    if (Note.addNote(note)) {
                        Utils.showToast(getActivity(), "Сохранено");

                        mListAdapter.notifyDataSetChanged();
                    }
                    else{
                        Utils.showToast(getActivity(), "Ошибка");
                    }
                }
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editBtn.setVisibility(View.GONE);
                applyEditBtn.setVisibility(View.VISIBLE);
                noteTE.setVisibility(View.VISIBLE);
                noteTE.setText(mSelectedNote.text);
                categorySpinner.setSelection(spinnerAdapter.getPosition(mSelectedNote.category));

            }
        });
        applyEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!noteTE.getText().toString().matches("")) {
                    mSelectedNote.text = noteTE.getText().toString();
                    mSelectedNote.category = (Category) categorySpinner.getSelectedItem();
                    Log.d("EDIT_SPINNER", mSelectedNote.category.getTitle());
                    Utils.showToast(getActivity(), "Изменено");
                    editBtn.setVisibility(View.VISIBLE);
                    applyEditBtn.setVisibility(View.GONE);  //TODO: очищать EditText
                    addBtn.setVisibility(View.VISIBLE);
                    mListAdapter.notifyDataSetChanged();
                }
                else {
                    Note.notesArrayList.remove(mSelectedNote);
                    Utils.showToast(getActivity(), "Удалено");
                    mListAdapter.notifyDataSetChanged();
                }
                try{
                    Note.serializeNotes();
                }catch (Exception e){e.printStackTrace();}
            }
        });

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

}
