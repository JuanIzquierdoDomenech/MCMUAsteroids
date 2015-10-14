package com.mcmu.juanjesus.mcmuasteroids.score_storage;

import android.content.Context;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class DOMScoreStorage implements ScoreStorage {

    private static String FILE = "DOM_scores.xml";
    private Context context;
    private Document document;
    private boolean documentLoaded;


    public DOMScoreStorage(Context context) {
        this.context = context;
        documentLoaded = false;
        Log.d("DOMScoreStorage", context.getFilesDir().toString() + "/" + FILE);
    }

    @Override
    public void saveScore(int score, String player, long date) {
        try {
            if(!documentLoaded) {
                readXML(context.openFileInput(FILE));
            }
        } catch (FileNotFoundException e) {
            createXML();
        } catch (Exception e) {
            e.printStackTrace();
        }

        addNewScore(score, player, date);

        try {
            writeXML(context.openFileOutput(FILE, Context.MODE_PRIVATE));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Vector<String> scoreList(int amount) {
        try {
            if(!documentLoaded) {
                readXML(context.openFileInput(FILE));
            }
        } catch (FileNotFoundException e) {
            createXML();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return toStringVector();
    }

    public void createXML() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.newDocument();
            Element root = document.createElement("dom_score_list");
            document.appendChild(root);
            documentLoaded = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readXML(InputStream in) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        document = builder.parse(in);
        documentLoaded = true;
    }

    public void writeXML(OutputStream os) throws Exception {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(os);
        transformer.transform(source, result);
    }

    public void addNewScore(int score, String player, long date) {
        Element scoreElement = document.createElement("score");
        scoreElement.setAttribute("date", String.valueOf(date));

        Element playerElement = document.createElement("player");
        Text playerText = document.createTextNode(player);
        playerElement.appendChild(playerText);
        scoreElement.appendChild(playerElement);

        Element pointsElement = document.createElement("points");
        Text pointsText = document.createTextNode(String.valueOf(score));
        pointsElement.appendChild(pointsText);
        scoreElement.appendChild(pointsElement);

        Element root = document.getDocumentElement();
        root.appendChild(scoreElement);
    }

    public Vector<String> toStringVector() {
        Vector<String> result = new Vector<>();
        String player = "";
        String points = "";
        Element root = document.getDocumentElement();

        NodeList scores = root.getElementsByTagName("score");
        for(int i = 0; i < scores.getLength(); i++) {
            Node score = scores.item(i);
            NodeList properties = score.getChildNodes();
            for (int j = 0; j < properties.getLength(); j++) {
                Node property = properties.item(j);
                String tag = property.getNodeName();
                if(tag.equals("player")) {
                    player = property.getFirstChild().getNodeValue();
                } else if (tag.equals("points")) {
                    points = property.getFirstChild().getNodeValue();
                }
            }
            result.add(player + " " + points);
        }
        return result;
    }
}
