package com.mcmu.juanjesus.mcmuasteroids.score_storage;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;

public class SocketScoreStorage implements ScoreStorage {

    private String ip = "158.42.146.127";
    private int port = 1234;

    public SocketScoreStorage() {

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
    }

    @Override
    public void saveScore(int score, String player, long date) {
        try {
            Socket sck = new Socket(ip, port);
            BufferedReader in = new BufferedReader(new InputStreamReader(sck.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter((sck.getOutputStream())), true);
            out.println(score + " " + player);
            String response = in.readLine();
            if(!response.equals("OK")) {
                Log.e("SocketScoreStorage", "Response server error: " + response);
            }
            sck.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Vector<String> scoreList(int amount) {
        Vector<String> result = new Vector<>();

        try {
            Socket sck = new Socket(ip, port);
            BufferedReader in = new BufferedReader(new InputStreamReader(sck.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(sck.getOutputStream()), true);
            out.println("PUNTUACIONES");
            int n = 0;
            String response;
            do {
                response = in.readLine();
                if (response != null) {
                    result.add(response);
                    n++;
                }
            } while (n < amount && response != null);
            sck.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
