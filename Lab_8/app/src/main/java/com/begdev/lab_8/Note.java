package com.begdev.lab_8;

import android.content.Context;
import android.util.Xml;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.stream.Stream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class Note {
    public static ArrayList<Note> notesArrayList = new ArrayList<>();

    Date date;
    String text;
    Category category;

    public Note(Date date, String noteText, @NonNull Category category) {
        this.date = date;
        this.text = noteText;
        this.category = category;
    }

    public Note() {
        this.date = null;
        this.text = null;
        this.category = new Category();
    }

    @Override
    public String toString() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM", Locale.ENGLISH);
        return this.category.getTitle() + " --> " + this.text + "   |   " + formatter.format(this.date);
    }


    public static boolean addNote(Note note) {
        int count = 0;
        for (Note existNote : notesArrayList
        ) {
            if (note.date.compareTo(existNote.date) == 0) {
                count++;
            }
        }
        if (count >= 5 || notesArrayList.size() >= 20) {
            return false;
        } else {
            notesArrayList.add(note);
            try {
                Note.serializeNotes();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
    }

    public static void serializeNotes() throws IOException {
        FileOutputStream fos = new FileOutputStream("/data/data/com.begdev.lab_8/files/base.xml");

        XmlSerializer xmlSerializer = Xml.newSerializer();
        xmlSerializer.setOutput(fos, "UTF-8");
        xmlSerializer.startDocument(null, Boolean.TRUE);
        xmlSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        xmlSerializer.startTag(null, "Root");
        for (int j = 0; j < notesArrayList.size(); j++) {
            xmlSerializer.startTag(null, "note");

            xmlSerializer.startTag(null, "text");
            xmlSerializer.text(notesArrayList.get(j).text);
            xmlSerializer.endTag(null, "text");

            xmlSerializer.startTag(null, "category");
            xmlSerializer.startTag(null, "id");
            xmlSerializer.text(Integer.toString(notesArrayList.get(j).category.getID()));
            xmlSerializer.endTag(null, "id");
            xmlSerializer.startTag(null, "title");
            xmlSerializer.text(notesArrayList.get(j).category.getTitle());
            xmlSerializer.endTag(null, "title");
            xmlSerializer.endTag(null, "category");

            xmlSerializer.startTag(null, "date");
            xmlSerializer.text(notesArrayList.get(j).date.toString());
            xmlSerializer.endTag(null, "date");

            xmlSerializer.endTag(null, "note");
        }
        xmlSerializer.endTag(null, "Root");
        xmlSerializer.endDocument();
        xmlSerializer.flush();
        fos.close();
    }

    public static void deserializeNotes(Context context) throws IOException, ParserConfigurationException, SAXException, ParseException {
        notesArrayList = new ArrayList<>();
        FileInputStream fis = context.openFileInput("base.xml");
        InputStreamReader isr = new InputStreamReader(fis);
        char[] dataChars = new char[fis.available()];
        isr.read(dataChars);
        isr.close();
        fis.close();
        String data = new String(dataChars);
        InputStream is = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document dom = db.parse(is);

        dom.getDocumentElement().normalize();
        NodeList items = dom.getElementsByTagName("note");

        for (int i = 0; i < items.getLength(); i++) {
            Note note = new Note();
            NodeList categoryNodes = null;
            Node item = items.item(i);
            NodeList parameters = item.getChildNodes();
            for (int j = 0; j < parameters.getLength(); j++) {
                Node parameter = parameters.item(j);
                if (parameter.getNodeName().equals("text"))
                    note.text = parameter.getFirstChild().getNodeValue();
                if (parameter.getNodeName().equals("date")) {
                    String dateValue = parameter.getFirstChild().getNodeValue();
                    Date date = new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy", Locale.ENGLISH).parse(dateValue);
                    note.date = date;
                }
                if (parameter.getNodeName().equals("category")) {
                    categoryNodes = parameter.getChildNodes();
                    for (int k = 0; k < categoryNodes.getLength(); k++) {
                        Node categoryParameter = categoryNodes.item(k);
                        if (categoryParameter.getNodeName().equals("id")) {
                            note.category.setID(Integer.parseInt(categoryParameter.getFirstChild().getNodeValue()));
                        }
                        if (categoryParameter.getNodeName().equals("title")) {
                            note.category.setTitile(categoryParameter.getLastChild().getNodeValue());
                        }
                    }
                }
            }
            notesArrayList.add(note);
        }
        for (Note note :
                Note.notesArrayList) {
            if (!Category.categoriesArrayList.contains(note.category)) {
                Category.categoriesArrayList.add(note.category);
            }
        }
    }
}
