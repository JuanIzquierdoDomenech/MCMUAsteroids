package com.mcmu.juanjesus.mcmuasteroids.score_storage;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.widget.Toast;

import com.mcmu.juanjesus.mcmuasteroids.R;

import java.util.Vector;

public class ContentProviderScoreStorage implements ScoreStorage {

    private Activity activity;

    public ContentProviderScoreStorage(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void saveScore(int score, String player, long date) {
        Uri uri = Uri.parse("content://org.example.scoresprovider/scores");
        ContentValues values = new ContentValues();
        values.put("player", player);
        values.put("points", score);
        values.put("date", date);

        try {
            activity.getContentResolver().insert(uri, values);
        } catch (Exception e) {
            Toast.makeText(activity, activity.getString(R.string.content_resolver_not_installed), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public Vector<String> scoreList(int amount) {
        Vector<String> result = new Vector<>();
        Uri uri = Uri.parse("content://org.example.scoresprovider/scores");
        Cursor cursor = activity.getContentResolver().query(uri, null, null, null, "date DESC");
        if(cursor != null) {
            while (cursor.moveToNext()) {
                String player = cursor.getString(cursor.getColumnIndex("player"));
                int score = cursor.getInt(cursor.getColumnIndex("points"));
                result.add(score + " " + player);
            }
        }

        return result;
    }
}
