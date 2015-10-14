package com.mcmu.juanjesus.mcmuasteroids.score_storage;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlSerializer;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class SAXScoreStorage implements ScoreStorage {

    private static String FILE = "sax_scores.xml";
    private Context context;
    private SAXScoreList list;
    private boolean listLoaded;

    public SAXScoreStorage(Context context) {
        this.context = context;
        list = new SAXScoreList();
        listLoaded = false;
        Log.d("SAXScoreStorage", context.getFilesDir().toString() + "/" + FILE);
    }

    @Override
    public void saveScore(int score, String player, long date) {
        try {
            if(!listLoaded) {
                list.readXML(context.openFileInput(FILE));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        list.addNewScore(score, player, date);
        try {
            list.writeXML(context.openFileOutput(FILE, Context.MODE_PRIVATE));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Vector<String> scoreList(int amount) {
        try {
            if(!listLoaded) {
                list.readXML(context.openFileInput(FILE));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list.toStringVector();
    }

    private class SAXScoreList {

        private List<Score> scoreList;

        public SAXScoreList() {
            scoreList = new ArrayList<>();
        }

        public void addNewScore(int score, String player, long date) {
            Score s = new Score();
            s.score = score;
            s.player = player;
            s.date = date;
            scoreList.add(s);
        }

        public Vector<String> toStringVector() {
            Vector<String> res = new Vector<>();
            for(Score s : scoreList) {
                res.add(s.player + " " + s.score);
            }
            return res;
        }

        public void readXML(InputStream in) throws Exception {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader reader = parser.getXMLReader();
            XMLHandler xmlHandler = new XMLHandler();
            reader.setContentHandler(xmlHandler);
            reader.parse(new InputSource(in));
            listLoaded = true;
        }

        public void writeXML(OutputStream os) {
            XmlSerializer serializer = Xml.newSerializer();
            try {
                serializer.setOutput(os, "UTF-8");
                serializer.startDocument("UTF-8", true);
                serializer.startTag("", "sax_score_list");
                for(Score score : scoreList) {
                    serializer.startTag("", "score");
                    serializer.attribute("", "date", String.valueOf(score.date));
                    serializer.startTag("", "player");
                    serializer.text(score.player);
                    serializer.endTag("", "player");
                    serializer.startTag("", "points");
                    serializer.text(String.valueOf(score.score));
                    serializer.endTag("", "points");
                    serializer.endTag("", "score");
                }
                serializer.endTag("", "sax_score_list");
                serializer.endDocument();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        private class Score {
            int score;
            String player;
            long date;
        }

        class XMLHandler extends DefaultHandler {

            private StringBuilder sb;
            private Score score;

            @Override
            public void startDocument() throws SAXException {
                scoreList = new ArrayList<>();
                sb = new StringBuilder();
            }

            @Override
            public void endDocument() throws SAXException {
            }

            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                sb.setLength(0);
                if(localName.equals("score")) {
                    score = new Score();
                    score.date = Long.parseLong(attributes.getValue("date"));
                }
            }

            @Override
            public void endElement(String uri, String localName, String qName) throws SAXException {
                if(localName.equals("points")) {
                    score.score = Integer.parseInt(sb.toString());
                } else if (localName.equals("player")) {
                    score.player = sb.toString();
                } else if (localName.equals("score")) {
                    scoreList.add(score);
                }
            }

            @Override
            public void characters(char[] ch, int start, int length) throws SAXException {
                sb.append(ch, start, length);
            }
        }
    }
}
