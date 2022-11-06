package com.begdev.lab_8;

import static java.lang.String.valueOf;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;


public class CategoryFragment extends Fragment {
    View rootView;
    ListView categoriesListView;
    Button addCategoryBtn, saveCategoryBtn, editCategoryBtn, applyEditCategoryBtn;
    ArrayAdapter<Category> adapter;
    Category mSelectedCategory;
    EditText addCategoryTE;
    TextView addCategoryTW;

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_category, container, false);
        categoriesListView = rootView.findViewById(R.id.categoryListView);
        editCategoryBtn = rootView.findViewById(R.id.editCategoryBtn);
        addCategoryTW = rootView.findViewById(R.id.addCategoryTW);
        addCategoryTE = rootView.findViewById(R.id.addCategoryTE);
        editCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setVisibility(View.GONE);
                applyEditCategoryBtn.setVisibility(View.VISIBLE);
                addCategoryTE.setVisibility(View.VISIBLE);
                addCategoryTE.setText(mSelectedCategory.getTitle());
            }
        });
        applyEditCategoryBtn = rootView.findViewById(R.id.applyEditCategoryBtn);
        applyEditCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addCategoryTE.getText().toString().matches("")) {
                    Category.categoriesArrayList.remove(mSelectedCategory);
                    Utils.showToast(getActivity(), "Удалено");
                    editCategoryBtn.setVisibility(View.VISIBLE);
                    addCategoryTE.setVisibility(View.GONE);
                    view.setVisibility(View.GONE);
                } else {
                    mSelectedCategory.setTitile(addCategoryTE.getText().toString());
                    try{
//                        Note.notesArrayList = null;
                        Note.serializeNotes();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    addCategoryTE.setVisibility(View.GONE);
                    view.setVisibility(View.GONE);
                    editCategoryBtn.setVisibility(View.VISIBLE);
                    Utils.showToast(getActivity(), "Изменено");
                }
                adapter.notifyDataSetChanged();

            }
        });
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, Category.categoriesArrayList);
        categoriesListView.setAdapter(adapter);
        categoriesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                for (int item = 0; item < adapter.getCount(); item++) {
                    View v = categoriesListView.getChildAt(item);
                    v.setBackgroundColor(Color.alpha(0));
                }
                mSelectedCategory = (Category) adapterView.getItemAtPosition(i);
                view.setBackgroundColor(Color.GREEN);
            }
        });


        saveCategoryBtn = rootView.findViewById(R.id.saveCategoryBtn);
        addCategoryBtn = rootView.findViewById(R.id.addCategoryBtn);
        saveCategoryBtn.setOnClickListener(onSaveBtnClick);
        addCategoryBtn.setOnClickListener(onAddBtnClick);
        return rootView;
    }

    View.OnClickListener onAddBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            TextView addCategoryTW;
//            EditText addCategoryTE;
            Button saveBtn;

            saveBtn = rootView.findViewById(R.id.saveCategoryBtn);
            saveBtn.setVisibility(View.VISIBLE);
            view.setVisibility(View.GONE);
            addCategoryTW.setVisibility(View.VISIBLE);
            addCategoryTE.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }
    };

    View.OnClickListener onSaveBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TextView addCategoryTW;
            EditText addCategoryTE;
            Button addBtn;
            addBtn = rootView.findViewById(R.id.addCategoryBtn);
            addBtn.setVisibility(View.VISIBLE);
            addCategoryTW = rootView.findViewById(R.id.addCategoryTW);
            addCategoryTE = rootView.findViewById(R.id.addCategoryTE);
            adapter.notifyDataSetChanged();
            String newCategory = addCategoryTE.getText().toString().replaceAll("\\s", "");

            if (!newCategory.matches("")) {
                if (Category.addCategory(newCategory)) {
                    Utils.showToast(getActivity(), "Добавлено");
                } else {
                    Utils.showToast(getActivity(), "Уже добавлено 5 категорий");
                }
            } else {
                Utils.showToast(getActivity(), "Ошибка ввода");
            }

//            Log.d("Ca", Category.categoriesArrayList.get(0).getTitle());
            Log.d("Ca", valueOf(Category.categoriesArrayList.size()));
            addCategoryTW.setVisibility(View.GONE);
            addCategoryTE.setText("");
            addCategoryTE.setVisibility(View.GONE);
            view.setVisibility(View.GONE);

        }
    };

}