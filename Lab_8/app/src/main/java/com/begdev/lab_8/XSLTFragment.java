package com.begdev.lab_8;

import static android.content.Context.MODE_PRIVATE;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.Calendar;
import java.util.Date;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class XSLTFragment extends Fragment {
    View rootView;
    Button generateBtn;
    CalendarView calendarView;
    Date mDate;


    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_xslt, container, false);
        generateBtn = rootView.findViewById(R.id.generateBtn);
        calendarView = rootView.findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                mDate = new Date(i, i1, i2);    //TODO: i - 1900
            }
        });

        generateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    createXSLT(mDate, getActivity());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return rootView;
    }

    public static void createXSLT(Date selectedDate, Context context) throws Exception {

        String xslt = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<xsl:stylesheet version=\"1.0\" exclude-result-prefixes=\"xsl\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n" +
                "<xsl:output method=\"xml\" encoding=\"UTF-8\"/>"+
                "\n"+
                "\n" +
                "<xsl:key name=\"notesGroup\" match=\"note[date = '"+selectedDate.toString()+"']\" use=\"category\"/>\n"+
                "\n" +
                "<xsl:template match=\"Root\">\n" +
                "    <category-list>\n" +
                "        <xsl:apply-templates select=\"note[(generate-id(.) = generate-id(key('notesGroup', category)))]\" />\n" +
                "</category-list>\n" +
                "</xsl:template>"+
                "\n" +
                "<xsl:template match=\"note\">\n" +
                "    <category id = \"{category/id}\" title = \"{category/title}\">\n" +
                "        <xsl:for-each select=\"key('notesGroup',category)\">\n" +
                "             <note>\n" +
                "                 <text><xsl:value-of select=\"text\"/></text>\n" +
                "                 <date><xsl:value-of select=\"date\"/></date>\n" +
                "             </note>        \n" +
                "        </xsl:for-each>\n" +
                "    </category>\n" +
                "</xsl:template>\t"+
                "\n" +
                "\n" +
                "</xsl:stylesheet>";

        File f = new File(context.getFilesDir(), "file.xslt");
        FileWriter fw = new FileWriter(f, false);
        fw.write(xslt);
        fw.close();
        FileInputStream xmlf = context.openFileInput("base.xml");
        FileInputStream xslf = context.openFileInput("file.xslt");
        FileOutputStream resultXML = context.openFileOutput("result.xml", MODE_PRIVATE);
        TransformerFactory tf = TransformerFactory.newInstance();
        TransformerFactory tfqwe = TransformerFactory.newInstance();
        Source xsltsrc = new StreamSource(xslf);
        Source xmlsrc = new StreamSource(xmlf);
        Transformer t = tf.newTransformer(xsltsrc);
        t.transform(xmlsrc, new StreamResult(resultXML));
    }
}