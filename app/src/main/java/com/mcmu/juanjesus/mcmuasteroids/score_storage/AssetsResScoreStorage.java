package com.mcmu.juanjesus.mcmuasteroids.score_storage;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

public class AssetsResScoreStorage implements ScoreStorage {

    private Context context;

    public AssetsResScoreStorage(Context context) {
        this.context = context;
    }

    @Override
    public void saveScore(int score, String player, long date) {

    }

    @Override
    public Vector<String> scoreList(int amount) {
        Vector<String> result = new Vector<>();
        try {
            InputStream f = context.getAssets().open("scores/scores.txt");
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
