package com.mcmu.juanjesus.mcmuasteroids.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.mcmu.juanjesus.mcmuasteroids.R;

public class AboutActivity extends AppCompatActivity {


    //region Create
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // Intent extras test
        getTestIntent();
    }
    //endregion


    //region Intents
    private void getTestIntent()
    {
        Bundle extras = getIntent().getExtras();
        String extrasString = extras.getString("SendIntent");
        Log.d("MCMUAsteroids", "Intent data (" + extrasString + ")");

        Intent resultIntent = new Intent();
        resultIntent.putExtra("ResponseIntent", "Intent response ok");
        setResult(RESULT_OK, resultIntent);
    }
    //endregion
}
