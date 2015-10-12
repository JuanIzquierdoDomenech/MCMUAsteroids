package com.mcmu.juanjesus.mcmuasteroids.score_storage;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.mcmu.juanjesus.mcmuasteroids.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Vector;

public class ExternalFileScoreStorage implements ScoreStorage {

    private static String SCORE_FILE = Environment.getExternalStorageDirectory() + "/scores.txt";
    private Context context;

    public ExternalFileScoreStorage(Context context) {
        this.context = context;
    }

    @Override
    public void saveScore(int score, String player, long date) {
        try {
            FileOutputStream f = new FileOutputStream(SCORE_FILE, true);
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

        String sdState = Environment.getExternalStorageState();
        if(!sdState.equals(Environment.MEDIA_MOUNTED)
                && !sdState.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            Toast.makeText(context, context.getString(R.string.cannot_read_ext_memory), Toast.LENGTH_SHORT).show();
            return result;
        }

        try {
            FileInputStream f = new FileInputStream(SCORE_FILE);
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
