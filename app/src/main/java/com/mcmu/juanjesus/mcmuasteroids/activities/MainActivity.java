package com.mcmu.juanjesus.mcmuasteroids.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.view.Menu;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mcmu.juanjesus.mcmuasteroids.R;
import com.mcmu.juanjesus.mcmuasteroids.score_storage.ArrayScoreStorage;
import com.mcmu.juanjesus.mcmuasteroids.score_storage.ScoreStorage;
import com.mcmu.juanjesus.mcmuasteroids.tasks.FactorialTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements GestureOverlayView.OnGesturePerformedListener {

    //region Private Member Variables

    // UI
    private TextView txtvAsteroidsTitle;
    private Button btnPlay;
    private Button btnPreferences;
    private Button btnAbout;
    private Button btnScores;
    private GestureOverlayView gestureOverlayView;

    // Picture
    private final static int TAKE_PIC_RCODE = 3;
    private Uri pictureUri;
    private ImageView imgTakePicture;
    private boolean pictureTaken = false;

    // Gesture
    private GestureLibrary gestLibrary;

    // Music
    private MediaPlayer mp;

    //endregion


    //region Public Member Variables
    public static ScoreStorage scoreStorage = new ArrayScoreStorage();
    //endregion


    //region Activity Lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Log.d("MainActivity", "-------------------------- onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Link UI elements to variables
        linkUI();

        // Animate text exercise
        playTestAnimations();

        // Listen for gestures
        setupGestureListener();

        // Create media player for audio
        mp = MediaPlayer.create(this, R.raw.pixelasteroid_audio);
    }

    @Override
    protected void onStart() {

        //Log.d("MainActivity", "-------------------------- onStart");

        super.onStart();

        if(!mp.isPlaying()) {
            mp.start();
        }
    }

    @Override
    protected void onResume() {

        //Log.d("MainActivity", "-------------------------- onResume");

        super.onResume();
    }

    @Override
    protected void onPause() {

        //Log.d("MainActivity", "-------------------------- onPause");

        super.onPause();
    }

    @Override
    protected void onStop() {

        //Log.d("MainActivity", "-------------------------- onStop");

        if (mp.isPlaying()) {
            mp.pause();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {

        //Log.d("MainActivity", "-------------------------- onDestroy");

        if (mp.isPlaying()) {
            mp.pause();
        }
        super.onDestroy();
    }

    //endregion


    //region Gestures

    private void setupGestureListener() {

        gestLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures);
        if (!gestLibrary.load()) finish();
        gestureOverlayView = (GestureOverlayView)findViewById(R.id.gestures_overlay_view);

        gestureOverlayView.addOnGesturePerformedListener(this);

    }

    @Override
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
        ArrayList<Prediction> predictions = gestLibrary.recognize(gesture);
        if (predictions.size() > 0) {
            String command = predictions.get(0).name;
            // Log.d("PREDICTION", command);
            switch (command) {
                case "play":
                    // Capitalized P
                    showGameActivity();
                    break;
                case "settings":
                    // Capitalized P
                    showPreferencesActivity();
                    break;
                case "about":
                    // Capitalized A
                    showAboutActivity();
                    break;
                case "scores":
                    // Non capitalized s
                    showScoresActivity();
                    break;
                case "exit":
                    // Capitalized E
                    exit();
                    break;
                default:
                    break;
            }
        }
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

        //Log.d("onSaveInstanceState", "onSaveInstanceState");

        if (pictureTaken) {
            outState.putString("pictureUri", pictureUri.toString());
        }

        if (mp != null) {
            outState.putInt("musicPosition", mp.getCurrentPosition());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        //Log.d("onRestoreInstanceState", "onRestoreInstanceState - " + pictureTaken);

        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState.getString("pictureUri") != null
                && savedInstanceState.getString("pictureUri").length() > 0) {

            pictureUri = Uri.parse(savedInstanceState.getString("pictureUri"));
            pictureTaken = true;
            imgTakePicture.setImageURI(pictureUri);
        }

        if (mp != null) {
            mp.seekTo(savedInstanceState.getInt("musicPosition"));
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
            case R.id.menu_calc_factorial:
                calculateFactorial();
                break;
            case R.id.menu_play_video:
                showVideoActivity();
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
        txtvAsteroidsTitle = (TextView)findViewById(R.id.txtv_asteroid_title);

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
                // Tween animations are not stopped when opening a new activity
                //Animation anims = AnimationUtils.loadAnimation(v.getContext(), R.anim.zoom_and_rotate);
                //btnAbout.startAnimation(anims);

                showAboutActivity();
            }
        });

        btnScores = (Button)findViewById(R.id.btn_asteroids_scores);
        btnScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showScoresActivity();
            }
        });

        imgTakePicture = (ImageView)findViewById(R.id.img_take_pic);
    }
    //endregion


    //region Private Methods
    private void showGameActivity() {
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

    private void showScoresActivity() {
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

    private void calculateFactorial() {

        FactorialTask factorialTask = new FactorialTask(this);
        factorialTask.execute(5);
    }

    private void showVideoActivity() {
        Intent videoActivityIntent = new Intent(getApplicationContext(), VideoActivity.class);
        startActivity(videoActivityIntent);
    }

    private void exit () {
        finish();
    }

    private void playTestAnimations() {

        Animation anims = AnimationUtils.loadAnimation(this, R.anim.zoom_and_rotate);
        txtvAsteroidsTitle.startAnimation(anims);

        anims = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        btnPlay.startAnimation(anims);

        anims = AnimationUtils.loadAnimation(this, R.anim.translate_right);
        btnPreferences.startAnimation(anims);

        anims = AnimationUtils.loadAnimation(this, R.anim.rotate_pivot_up_left);
        btnAbout.startAnimation(anims);

        anims = AnimationUtils.loadAnimation(this, R.anim.rotate_pivot_down_right);
        btnScores.startAnimation(anims);
    }

    //endregion

}
