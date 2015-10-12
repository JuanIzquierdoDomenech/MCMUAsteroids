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

import butterknife.Bind;
import butterknife.ButterKnife;

public class GameActivity extends AppCompatActivity {

    //region Private Member Variables

    @Bind(R.id.game_view) GameView gameView;

    //endregion


    //region Activity lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Inject butter knife dependencies
        ButterKnife.bind(this);

        gameView.setParentActivity(this);
    }

    @Override
    protected void onResume() {

        super.onResume();
        gameView.enableSensors();
        gameView.gameThread.resumeThread();
    }

    @Override
    protected void onPause() {

        super.onPause();
        gameView.disableSensors();
        gameView.gameThread.pauseThread();
    }

    @Override
    protected void onDestroy() {

        gameView.gameThread.destroyThread();
        super.onDestroy();
    }
    //endregion
}
