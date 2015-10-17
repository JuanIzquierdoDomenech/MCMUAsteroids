package com.mcmu.juanjesus.mcmuasteroids.score_storage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Vector;

public class SQLiteScoreStorage extends SQLiteOpenHelper implements ScoreStorage{


    public SQLiteScoreStorage(Context context) {
        super(context, "scores", null, 1);
    }

    @Override
    public void saveScore(int score, String player, long date) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO scores VALUES ( null, " + score + " , '" + player + "' , " + date + " ) ");
        db.close();
    }

    @Override
    public Vector<String> scoreList(int amount) {
        Vector<String> result = new Vector<>();
        SQLiteDatabase db = getReadableDatabase();
        String[] fields = {"points", "player"};
        Cursor cursor = db.query("scores", fields, null, null, null, null, "points DESC", Integer.toString(amount));
        while (cursor.moveToNext()) {
            result.add(cursor.getInt(0) + " " + cursor.getString(1));
        }
        cursor.close();
        db.close();
        return result;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE scores ( _id INTEGER PRIMARY KEY AUTOINCREMENT, points INTEGER, player TEXT, date LONG ) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
