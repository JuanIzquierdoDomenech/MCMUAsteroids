package com.mcmu.juanjesus.mcmuasteroids.score_storage;

import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.mcmu.juanjesus.mcmuasteroids.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SocketScoreStorage implements ScoreStorage {

    private String ip = "158.42.146.127";
    private int port = 1234;
    private Context context;

    public SocketScoreStorage(Context context) {

        this.context = context;
        // StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
    }

    @Override
    public void saveScore(int score, String player, long date) {
        Log.d("Asteroids", "SAVING");
        try {
            SaveScoreTask saveScoreTask = new SaveScoreTask();
            saveScoreTask.execute(String.valueOf(score), player, String.valueOf(date));
            saveScoreTask.get(10, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            Toast.makeText(context, context.getString(R.string.error_time_exceed), Toast.LENGTH_SHORT).show();
        } catch (CancellationException e) {
            Toast.makeText(context, context.getString(R.string.error_connecting_server), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, context.getString(R.string.error_async_task), Toast.LENGTH_SHORT).show();
        }
    }

    private class SaveScoreTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            String score = params[0];
            String player = params[1];
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
            return null;
        }
    }

    @Override
    public Vector<String> scoreList(int amount) {
        Log.d("Asteroids", "LISTING");
        Vector<String> result = new Vector<>();
        try {
            GetScoreListTask getScoresTask = new GetScoreListTask();
            getScoresTask.execute(amount);
            result = getScoresTask.get(10, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            Toast.makeText(context, context.getString(R.string.error_time_exceed), Toast.LENGTH_SHORT).show();
        } catch (CancellationException e) {
            Toast.makeText(context, context.getString(R.string.error_connecting_server), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, context.getString(R.string.error_async_task), Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    private class GetScoreListTask extends AsyncTask<Integer, Void, Vector<String>> {

        @Override
        protected Vector<String> doInBackground(Integer... params) {
            Vector<String> result = new Vector<>();
            int amount = params[0];
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
}
