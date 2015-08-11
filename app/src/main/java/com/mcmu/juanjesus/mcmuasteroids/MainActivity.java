package com.mcmu.juanjesus.mcmuasteroids;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //region Private Member Variables
    private Button btnPlay;
    private Button btnPreferences;
    private Button btnAbout;
    private Button btnExit;
    //endregion


    //region Create
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linkUI();
    }
    //endregion


    //region Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //boolean response = false;
        switch(id) {
            case R.id.menu_settings:
                showPreferencesActivity();
                break;
            case R.id.menu_about:
                showAboutActivity();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    //endregion


    //region Intents
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        if (requestCode == 9999
                && resultCode == RESULT_OK) {
            String responseData = data.getExtras().getString("ResponseIntent");
            Log.d("MCMUAsteroids", "Intent response returned successfully -> " + responseData);
        }
    }
    //endregion


    //region UI
    private void linkUI()
    {
        btnPlay = (Button)findViewById(R.id.btn_asteroids_play);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPlayActivity();
            }
        });

        btnPreferences = (Button)findViewById(R.id.btn_asteroids_options);
        btnPreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPreferencesActivity();
            }
        });

        btnAbout = (Button)findViewById(R.id.btn_asteroids_about);
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAboutActivity();
            }
        });

        btnExit = (Button)findViewById(R.id.btn_asteroids_exit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showPlayActivity() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String s = "Music: " + pref.getBoolean("music", true)
                + " Graphics: " + pref.getString("graphics_level", "?");
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private void showPreferencesActivity() {
        Intent preferencesActivityIntent = new Intent(getApplicationContext(), PreferencesActivity.class);
        startActivity(preferencesActivityIntent);
    }

    private void showAboutActivity() {
        Intent aboutActivityIntent = new Intent(getApplicationContext(), AboutActivity.class);
        aboutActivityIntent.putExtra("SendIntent", "This is some data :D");
        startActivity(aboutActivityIntent);
        //startActivityForResult(aboutActivityIntent, 9999);
    }

    //endregion

}
