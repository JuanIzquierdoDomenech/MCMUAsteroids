package com.mcmu.juanjesus.mcmuasteroids.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.Toast;

import com.mcmu.juanjesus.mcmuasteroids.R;
import com.mcmu.juanjesus.mcmuasteroids.score_storage.ArrayScoreStorage;
import com.mcmu.juanjesus.mcmuasteroids.score_storage.ScoreStorage;

import java.io.File;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //region Private Member Variables
    private Button btnPlay;
    private Button btnPreferences;
    private Button btnAbout;
    private Button btnExit;

    private final static int TAKE_PIC_RCODE = 3;
    private Uri pictureUri;
    private ImageView imgTakePicture;
    private boolean pictureTaken = false;
    //endregion


    //region Public Member Variables
    public static ScoreStorage scoreStorage = new ArrayScoreStorage();
    //endregion


    //region Create
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linkUI();
    }
    //endregion


    //region Intents Results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*if (requestCode == 9999
                && resultCode == RESULT_OK) {
            String responseData = data.getExtras().getString("ResponseIntent");
            Log.d("MCMUAsteroids", "Intent response returned successfully -> " + responseData);
        }*/

        if (requestCode == TAKE_PIC_RCODE
                && resultCode == RESULT_OK
                && pictureUri != null) {

            pictureTaken = true;
            imgTakePicture.setImageURI(pictureUri);
        }
    }
    //endregion


    //region Save And Restore
    @Override
    protected void onSaveInstanceState(Bundle outState) {

        Log.d("onSaveInstanceState", "onSaveInstanceState");
        if (pictureTaken) {
            outState.putString("pictureUri", pictureUri.toString());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        Log.d("onRestoreInstanceState", "onRestoreInstanceState - " + pictureTaken);
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.getString("pictureUri") != null
                && savedInstanceState.getString("pictureUri").length() > 0) {

            pictureUri = Uri.parse(savedInstanceState.getString("pictureUri"));
            pictureTaken = true;
            imgTakePicture.setImageURI(pictureUri);
        }
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
        switch(id) {
            case R.id.menu_settings:
                showPreferencesActivity();
                break;
            case R.id.menu_about:
                showAboutActivity();
                break;
            case R.id.menu_share:
                shareSomethingRandom();
                break;
            case R.id.menu_location:
                openAlcoyOnGoogleMaps();
                break;
            case R.id.menu_take_picture:
                takePicture();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    //endregion


    //region UI
    private void linkUI()
    {
        btnPlay = (Button)findViewById(R.id.btn_asteroids_play);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGameActivity();
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
                showScoreActivity();
                //exit();
            }
        });

        imgTakePicture = (ImageView)findViewById(R.id.img_take_pic);
    }
    //endregion


    //region Private Methods
    private void showGameActivity() {
        /*SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String s = "Music: " + pref.getBoolean("music", true)
                + " Graphics: " + pref.getString("graphics_level", "?");
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();*/
        Intent gameActivityIntent = new Intent(getApplicationContext(), GameActivity.class);
        startActivity(gameActivityIntent);
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

    private void showScoreActivity() {
        Intent scoresActivityIntent = new Intent(getApplicationContext(), ScoresActivity.class);
        startActivity(scoresActivityIntent);
    }

    private void shareSomethingRandom() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Sharing smzing");
        startActivity(shareIntent);
    }

    private void openAlcoyOnGoogleMaps() {
        /*LocationManager locationManager =
                (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        GPSLocationListener gpsLocationListener = new GPSLocationListener();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsLocationListener);*/

        String uri = String.format(Locale.ENGLISH, "geo:%f,%f", 38.6987672f, -0.47185420989990234f);
        Intent mapsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(mapsIntent);
    }

    private void takePicture() {

        // For example /storage/emulated/0/img_1439994470.jpg
        String picturePath = Environment.getExternalStorageDirectory() + File.separator + "img_" + (System.currentTimeMillis()/1000) + ".jpg";
        pictureUri = Uri.fromFile(new File(picturePath));

        Intent pictureIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);    // Asks to store the requested uri
        startActivityForResult(pictureIntent, TAKE_PIC_RCODE);
    }

    private void exit () {
        finish();
    }
    //endregion

}
