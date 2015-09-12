package com.mcmu.juanjesus.mcmuasteroids.tasks;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.widget.Toast;

import com.mcmu.juanjesus.mcmuasteroids.activities.MainActivity;

public class FactorialTask extends AsyncTask<Integer, Integer, Integer>{

    // region Private Member Variables

    private Activity callingActivity;
    private ProgressDialog progressDialog;

    // endregion


    public FactorialTask(Activity callingActivity) {
        this.callingActivity = callingActivity;
        progressDialog = new ProgressDialog(this.callingActivity);
    }

    @Override
    protected void onPreExecute() {

        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(true);
        progressDialog.setMax(100);
        progressDialog.setProgress(0);
        progressDialog.show();

        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                FactorialTask.this.cancel(true);
            }
        });
    }

    @Override
    protected Integer doInBackground(Integer... n) {

        int res = 1;
        for (int i = 1; i <= n[0] && !isCancelled(); i++) {
            res *= i;
            SystemClock.sleep(1000);
            publishProgress(i*100 / n[0]);
        }
        return res;
    }

    @Override
    protected void onProgressUpdate(Integer... precentage) {

        progressDialog.setProgress(precentage[0]);
    }

    @Override
    protected void onPostExecute(Integer result) {

        progressDialog.dismiss();
        Toast.makeText(callingActivity, "Factorial: " + result, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCancelled() {

    }
}
