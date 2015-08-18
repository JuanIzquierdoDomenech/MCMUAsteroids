package com.mcmu.juanjesus.mcmuasteroids.activities;


import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.mcmu.juanjesus.mcmuasteroids.R;

public class PreferencesActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.player_preferences);
    }

}
