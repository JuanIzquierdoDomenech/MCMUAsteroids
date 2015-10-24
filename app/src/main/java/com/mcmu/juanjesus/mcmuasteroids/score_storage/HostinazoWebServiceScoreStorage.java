package com.mcmu.juanjesus.mcmuasteroids.score_storage;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.mcmu.juanjesus.mcmuasteroids.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Vector;
import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class HostinazoWebServiceScoreStorage implements ScoreStorage {

    // Example URLs
    // http://jizquierdo.hostinazo.com/puntuaciones/lista.php?max=10
    // http://jizquierdo.hostinazo.com/puntuaciones/nueva.php?puntos=1232&nombre=Superman&fecha=0

    private String host = "jizquierdo.hostinazo.com";
    private Context context;

    public HostinazoWebServiceScoreStorage(Context context) {

        // StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        this.context = context;
    }

    @Override
    public void saveScore(int score, String player, long date) {
        Log.d("Asteroids", "SAVING");
        try {
            SaveScoreTask saveScoreTask = new SaveScoreTask();
            saveScoreTask.execute(String.valueOf(score), player, String.valueOf(date));
            saveScoreTask.get(4, TimeUnit.SECONDS);
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
            try {
                /*URL url = new URL("http://" + host + "/asteroids_scores/add_score.php" +
                        "?newscore=" + params[0] +
                        "&player=" + URLEncoder.encode(params[1], "UTF-8") +
                        "&date=" + params[2]);*/
                URL url = new URL("http://" + host + "/puntuaciones/nueva.php" +
                        "?puntos=" + params[0] +
                        "&nombre=" + URLEncoder.encode(params[1], "UTF-8") +
                        "&fecha=" + params[2]);
                Log.d("Asteroids", url.toString());
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line = reader.readLine();
                    if(!line.equals("OK")) {
                        cancel(true);
                        Log.e("Asteroids", "Asteroids Service Error");
                    }
                } else {
                    cancel(true);
                    Log.e("Asteroids", connection.getResponseMessage());
                }
                connection.disconnect();
            } catch (Exception e) {
                cancel(true);
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
            result = getScoresTask.get(4, TimeUnit.SECONDS);
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
            try {
                // URL url = new URL("http://" + host + "/asteroids_scores/score_list.php?max=" + params[0]);
                URL url = new URL("http://" + host + "/puntuaciones/lista.php?max=" + params[0]);
                Log.d("Asteroids", url.toString());
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
                    Log.e("Asteroids", "CONNECTION ERROR: " + connection.getResponseMessage());
                    connection.disconnect();
                    cancel(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
                cancel(true);
            }
            return result;
        }
    }
}
