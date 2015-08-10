package com.mcmu.juanjesus.mcmuasteroids;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // Intent extras test
        Bundle extras = getIntent().getExtras();
        String extrasString = extras.getString("SomeData");
        Log.d("Intent extras: ", extrasString);
    }
}
