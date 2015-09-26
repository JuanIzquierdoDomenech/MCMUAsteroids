package com.mcmu.juanjesus.mcmuasteroids.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import com.mcmu.juanjesus.mcmuasteroids.R;

import butterknife.ButterKnife;

public class GoogleMapsActivity extends FragmentActivity {

    //region Activity lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);

        // Inject butter knife dependencies
        ButterKnife.bind(this);
    }
    /*

    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
    */
    //endregion
}
