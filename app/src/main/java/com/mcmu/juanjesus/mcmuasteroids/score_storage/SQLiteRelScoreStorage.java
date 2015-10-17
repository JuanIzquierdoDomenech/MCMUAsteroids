package com.mcmu.juanjesus.mcmuasteroids.score_storage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Vector;

public class SQLiteRelScoreStorage extends SQLiteOpenHelper implements ScoreStorage{


    public SQLiteRelScoreStorage(Context context) {
        super(context, "scores", null, 2);
    }

    @Override
    public void saveScore(int score, String player, long date) {
        SQLiteDatabase db = getWritableDatabase();
        savesScore(db, score, player, date);
        db.close();
    }

    public void savesScore(SQLiteDatabase db, int score, String player, long date) {
        int user = lookOrInsert(db, player);
        db.execSQL("PRAGMA foreign_keys = ON");
        db.execSQL("INSERT INTO scores2 VALUES (null, " + score + ", " + date + ", " + user + ")");
    }

    private int lookOrInsert(SQLiteDatabase db, String player) {
        Cursor cursor = db.rawQuery("SELECT user_id FROM users WHERE name = '" + player + "'", null);
        if (cursor.moveToNext()) {

            int result = cursor.getInt(0);
            cursor.close();
            return result;
        } else {
            cursor.close();
            db.execSQL("INSERT INTO users VALUES (null, '" + player + "', 'mail@domain.com')");
            return lookOrInsert(db, player);
        }
    }

    @Override
    public Vector<String> scoreList(int amount) {
        Vector<String> result = new Vector<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT points, name FROM scores2, users WHERE user = user_id ORDER BY points DESC LIMIT " + amount, null);
        while (cursor.moveToNext()) {
            result.add(cursor.getInt(0) + " " + cursor.getString(1));
        }
        cursor.close();
        db.close();
        return result;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users ( user_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, mail TEXT )");
        db.execSQL("CREATE TABLE scores2 ( score_id INTEGER PRIMARY KEY AUTOINCREMENT, points INTEGER, date LONG, user INTEGER, FOREIGN KEY (user) REFERENCES users(user_id) )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("SQLiteRelScoreStorage", "Upgrading DB to version 2");
        if(oldVersion == 1
                && newVersion == 2) {
            onCreate(db);
            Cursor cursor = db.rawQuery("SELECT points, player, date FROM scores", null);
            while(cursor.moveToNext()) {
                savesScore(db, cursor.getInt(0), cursor.getString(1), cursor.getInt(2));
            }
            cursor.close();
            db.execSQL("DROP TABLE scores");
        }
    }
}
