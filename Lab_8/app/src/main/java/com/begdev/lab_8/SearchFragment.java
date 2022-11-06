package com.begdev.lab_8;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.documentfile.provider.DocumentFile;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class SearchFragment extends Fragment {
    View rootView;
    ListView resultListView;
    Spinner categorySpinner;
    ArrayList<Note> resultNotesArrayList;
    ArrayAdapter<Category> mSpinnerAdapter;
    ArrayAdapter<Note> mListAdapter;


    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search, container, false);
        resultListView = rootView.findViewById(R.id.resultListView);
        resultListView.setAdapter(mListAdapter);
        categorySpinner = rootView.findViewById(R.id.spinnerCategories);
        mSpinnerAdapter = new ArrayAdapter<Category>(getActivity(), android.R.layout.simple_spinner_item, Category.categoriesArrayList);
        mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(mSpinnerAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //TODO: обновить list adapter
                try {
                    mListAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, searchByCategory((Category) adapterView.getSelectedItem()));
                    resultListView.setAdapter(mListAdapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

//        resultListView.setAdapter();
        return rootView;
    }

    public ArrayList<Note> searchByCategory(Category category) throws IOException, ParserConfigurationException, ParseException,SAXException, XPathExpressionException {
        File f = new File(rootView.getContext().getFilesDir(), "base.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(f);
        XPath x = XPathFactory.newInstance().newXPath();
        String expression = "/Root/note[category/title='" + category.getTitle() + "']";
        NodeList nodeList = (NodeList) x.compile(expression).evaluate(doc, XPathConstants.NODESET);
        ArrayList<Note> resultArray = new ArrayList<>();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            Element taskBuffer = (Element) node;
            String text = taskBuffer.getElementsByTagName("text").item(0).getTextContent();
            String dateValue = taskBuffer.getElementsByTagName("date").item(0).getTextContent();
            Date date = new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy", Locale.ENGLISH).parse(dateValue);
            Note note = new Note();
            note.text = text;
            note.date = date;
            note.category = category;
//            note.date = Date.from(date);
//            note.Category=Category;
            resultArray.add(note);
        }
        return resultArray;
    }

}
