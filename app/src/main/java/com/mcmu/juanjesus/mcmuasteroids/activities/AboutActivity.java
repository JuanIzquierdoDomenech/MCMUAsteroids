package com.mcmu.juanjesus.mcmuasteroids.activities;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcmu.juanjesus.mcmuasteroids.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AboutActivity extends AppCompatActivity {

    //region Private Member Variables
    @Bind(R.id.txtv_about) TextView txtvAbout;
    @Bind(R.id.img_test_about) ImageView imgTest;
    //endregion


    //region Create
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // Inject butter knife dependencies
        ButterKnife.bind(this);

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

        Animation textTweenAnim = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        txtvAbout.startAnimation(textTweenAnim);

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
