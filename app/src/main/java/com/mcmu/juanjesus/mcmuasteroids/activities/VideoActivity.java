package com.mcmu.juanjesus.mcmuasteroids.activities;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mcmu.juanjesus.mcmuasteroids.R;

import org.w3c.dom.Text;

import butterknife.Bind;
import butterknife.ButterKnife;

public class VideoActivity extends Activity implements MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, SurfaceHolder.Callback{

    private MediaPlayer mediaPlayer;

    @Bind(R.id.videoview) SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;

    @Bind(R.id.edittext_video_path) EditText editText;
    @Bind(R.id.imgbtn_play) ImageButton bPlay;
    @Bind(R.id.imgbtn_pause) ImageButton bPause;
    @Bind(R.id.imgbtn_stop) ImageButton bStop;
    @Bind(R.id.imgbtn_log) ImageButton bLog;
    @Bind(R.id.txtv_video_log) TextView txtvLog;
    
    private boolean pause;
    private String path;
    private int savePos = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        // Inject butter knife dependencies
        ButterKnife.bind(this);

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        editText.setText("http://personales.upv.es/~jtomas/video.3gp");

        bPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer != null) {
                    if(pause) {
                        mediaPlayer.start();
                    } else {
                        playVideo();
                    }
                }
            }
        });

        bPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null) {
                    pause = true;
                    mediaPlayer.pause();
                }
            }
        });

        bStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null
                        && mediaPlayer.isPlaying()) {
                    pause = false;
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                }
            }
        });

        bLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtvLog.getVisibility() == TextView.VISIBLE) {
                    txtvLog.setVisibility(TextView.INVISIBLE);
                } else {
                    txtvLog.setVisibility(TextView.VISIBLE);
                }
            }
        });

        log("");
    }

    private void playVideo() {

        try {
            pause = false;
            path = editText.getText().toString();
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(path);
            mediaPlayer.setDisplay(surfaceHolder);
            mediaPlayer.prepare();
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.seekTo(savePos);
        } catch(Exception e) {
            log("Error: " + e.getMessage());
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        log("onBufferingUpdate percent: " + percent);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        log("onCompletion called");
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        log("onPrepared called");
        int mVideoWidth = mp.getVideoWidth();
        int mVideoHeight = mp.getVideoHeight();
        if(mVideoHeight != 0 && mVideoWidth != 0) {
            surfaceHolder.setFixedSize(mVideoWidth, mVideoHeight);
            mediaPlayer.start();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        log("surfaceCreated called");
        playVideo();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        log("surfaceChanged called");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        log("surfaceDestroyed called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && !pause) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mediaPlayer != null && !pause) {
            mediaPlayer.start();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mediaPlayer != null) {
            int pos = mediaPlayer.getCurrentPosition();
            outState.putString("path", path);
            outState.putInt("pos", pos);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null) {
            path = savedInstanceState.getString("path");
            savePos = savedInstanceState.getInt("pos");
        }
    }

    private void log(String s) {
        txtvLog.append(s + "\n");
        Log.d("VideoActivity", s);
    }
}
