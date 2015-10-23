package com.mcmu.juanjesus.mcmuasteroids.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcmu.juanjesus.mcmuasteroids.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.Buffer;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GoogleSearchActivity extends AppCompatActivity {

    //region Private Member Variables
    @Bind(R.id.search_in_google_btn) Button btnSearch;
    @Bind(R.id.search_in_google_word) EditText wordEditText;
    @Bind(R.id.search_in_google_results_view) TextView resultsView;
    //endregion

    //region Activity Lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_google_search);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

        ButterKnife.bind(this);
    }
    //endregion


    @OnClick(R.id.search_in_google_btn)
    protected void search() {
        // On main thread
        /*try {
            String word = wordEditText.getText().toString();
            String result = googleResult(word);
            resultsView.append(result);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        // On Async Task
        String word = wordEditText.getText().toString();
        resultsView.append(word + " --> ");
        new SearchInGoogleTask().execute(word);
    }

    private String googleResult(String words) throws Exception{
        String site = "", result = "";
        URL url = new URL("http://www.google.es/search?hl=es&q=\"" + URLEncoder.encode(words, "UTF-8") + "\"");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1)");

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = reader.readLine();
            while (line != null) {
                site += line;
                line = reader.readLine();
            }
            reader.close();
            int index = site.indexOf("Aproximadamente");
            if (index != -1) {
                int end = site.indexOf(" ", index + 16);
                result = site.substring(index + 16, end);
            } else {
                result = getString(R.string.not_found);
            }
            result += "\n";
        } else {
            resultsView.append(connection.getResponseMessage());
        }
        connection.disconnect();
        return result;
    }

    private class SearchInGoogleTask extends AsyncTask<String, Void, String> {

        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(GoogleSearchActivity.this);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setMessage(getString(R.string.downloading_magic));
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                return googleResult(params[0]);
            } catch (Exception e) {
                cancel(true);
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progress.dismiss();
            resultsView.append(result + "\n");
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            progress.dismiss();
            resultsView.append(getString(R.string.error_connecting) + "\n");
        }
    }
}
