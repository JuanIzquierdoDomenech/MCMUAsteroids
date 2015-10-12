package com.mcmu.juanjesus.mcmuasteroids.score_storage;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Vector;

public class InternalFileScoreStorage implements ScoreStorage {

    private static String SCORE_FILE = "scores.txt";
    private Context context;

    public InternalFileScoreStorage(Context context) {
        this.context = context;
    }

    @Override
    public void saveScore(int score, String player, long date) {
        try {
            FileOutputStream f = context.openFileOutput(SCORE_FILE, Context.MODE_APPEND);
            String text = score + " " + player + "\n";
            f.write(text.getBytes());
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Vector<String> scoreList(int amount) {
        Vector<String> result = new Vector<>();
        try {
            FileInputStream f = context.openFileInput(SCORE_FILE);
            BufferedReader bf = new BufferedReader(new InputStreamReader(f));

            int n = 0;
            String current;

            do {
                current = bf.readLine();
                if(current != null) {
                    result.add(current);
                    n++;
                }
            } while(n < amount && current != null);
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
