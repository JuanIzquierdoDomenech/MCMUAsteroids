package com.mcmu.juanjesus.mcmuasteroids.score_storage;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Vector;

public class PreferencesScoreStorage implements ScoreStorage {

    private static String PREFERENCES_FILE = "scores";
    private Context context;

    public PreferencesScoreStorage(Context context) {
        this.context = context;
    }

    @Override
    public void saveScore(int score, String player, long date) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        for(int n = 9; n >= 1; n--) {
            editor.putString("score" + n, prefs.getString("score" + (n-1), ""));
        }
        editor.putString("score0", score + " " + player);

        editor.commit();
    }

    @Override
    public Vector<String> scoreList(int amount) {
        Vector<String> result = new Vector<>();
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        for(int n = 0; n <= 9; n++) {
            String s = prefs.getString("score" + n, "");
            result.add(s);
        }
        return result;
    }
}
