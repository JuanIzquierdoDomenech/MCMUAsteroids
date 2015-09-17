package com.mcmu.juanjesus.mcmuasteroids.activities;

import android.app.Application;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mcmu.juanjesus.mcmuasteroids.R;
import com.mcmu.juanjesus.mcmuasteroids.views.GameView;

import java.util.List;

public class GameActivity extends AppCompatActivity {

    //region Private Member Variables

    private GameView gameView;

    //endregion


    //region Activity lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameView = (GameView)findViewById(R.id.game_view);
    }

    @Override
    protected void onResume() {

        super.onResume();
        gameView.gameThread.resumeThread();
    }

    @Override
    protected void onPause() {

        super.onPause();
        gameView.gameThread.pauseThread();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        gameView.gameThread.destroyThread();
    }
    //endregion
}
