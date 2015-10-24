package com.mcmu.juanjesus.mcmuasteroids.score_storage;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Vector;

public class LocalXAMMPWebServiceScoreStorage implements ScoreStorage {

    private String host = "192.168.1.20";

    @Override
    public void saveScore(int score, String player, long date) {
        try {
            URL url = new URL("http://" + host + "/asteroids_scores/add_score.php" +
            "?newscore=" + score +
            "&player=" + URLEncoder.encode(player, "UTF-8") +
            "&date=" + date);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = reader.readLine();
                if(!line.equals("OK")) {
                    Log.e("Asteroids", "Asteroids Service Error");
                }
            } else {
                Log.e("Asteroids", connection.getResponseMessage());
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Vector<String> scoreList(int amount) {
        Vector<String> result = new Vector<>();
        try {
            URL url = new URL("http://" + host + "/asteroids_scores/score_list?max=" + amount);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = reader.readLine();
                while (!line.equals("")) {
                    result.add(line);
                    line = reader.readLine();
                }
                reader.close();
                connection.disconnect();
            } else {
                Log.e("Asteroids", connection.getResponseMessage());
                connection.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
