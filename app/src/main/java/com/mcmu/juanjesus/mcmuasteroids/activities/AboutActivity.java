package com.mcmu.juanjesus.mcmuasteroids.activities;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.mcmu.juanjesus.mcmuasteroids.R;

public class AboutActivity extends AppCompatActivity {

    //region Private Member Variables
    private ImageView imgTest;
    //endregion


    //region Create
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        imgTest = (ImageView)findViewById(R.id.img_test_about);

        // Transition test
        /*TransitionDrawable transition = (TransitionDrawable)getResources().getDrawable(R.drawable.asteroid_transition);
        imgTest.setImageDrawable(transition);
        transition.startTransition(2000);*/

        // Animation test
        final AnimationDrawable pikachuAnim = (AnimationDrawable)getResources().getDrawable(R.drawable.pikachu_animation);
        imgTest.setImageDrawable(pikachuAnim);
        imgTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pikachuAnim.start();
            }
        });

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
