package com.mcmu.juanjesus.mcmuasteroids.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.graphics.drawable.shapes.RectShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.mcmu.juanjesus.mcmuasteroids.R;
import com.mcmu.juanjesus.mcmuasteroids.graphics.Graphic;

import java.util.*;

public class GameView extends View implements SensorEventListener {

    //region Private Member Variables

    // Asteroids
    private Vector<Graphic> asteroids;
    private int numAsteroids = 5;
    private int numFragments = 3;

    // Spaceship
    private Graphic spaceship;
    private int spaceshipGyre;
    private double spaceshipAcceleration;
    private static final int MAX_SPACECHIP_VEL = 20;
    private static final int SPACESHIP_GYRE_INC = 5;
    private static final float SPACESHIP_ACCELERATION_INC = 0.5f;

    // Missiles
    private Vector<Graphic> missiles;
    private Vector<Boolean> isMissileActive;
    private int ammo = 5;
    private static int MISSILE_VEL = 12;
    //public boolean isMissileActive = false;
    //private int missileTime;

    // Threading and timing
    private GameThread thread = new GameThread();
    private static int PROCESS_PERIOD = 50; // How often do we want to process changes
    private long lastProcess = 0;

    // Touch control
    private float mX, mY = 0;
    private boolean shooting = false;

    // Sensors
    private SensorManager mSensorManager;
    private boolean orientationSensorHasInitialValue = false;
    private float orientationSensorInitialValue;

    //endregion


    public GameView(Context ctx, AttributeSet attrs) {

        super(ctx, attrs);

        mSensorManager = (SensorManager)getContext().getSystemService(Context.SENSOR_SERVICE);

        Drawable spaceshipDrawable, asteroidDrawable, missileDrawable;

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());

        // Choose graphics preferences to select drawable type
        if (pref.getString("graphics_level", "1").equals("0")) {

            ////////////////////////////////////////////////////////// Vector mode

            // Asteroids
            Path asteroidPath = new Path();
            asteroidPath.moveTo(0.3f, 0.0f);
            asteroidPath.lineTo(0.6f, 0.0f);
            asteroidPath.lineTo(0.6f, 0.3f);
            asteroidPath.lineTo(0.8f, 0.2f);
            asteroidPath.lineTo(1.0f, 0.4f);
            asteroidPath.lineTo(0.8f, 0.6f);
            asteroidPath.lineTo(0.9f, 0.9f);
            asteroidPath.lineTo(0.8f, 1.0f);
            asteroidPath.lineTo(0.4f, 1.0f);
            asteroidPath.lineTo(0.0f, 0.6f);
            asteroidPath.lineTo(0.0f, 0.2f);
            asteroidPath.lineTo(0.3f, 0.0f);
            ShapeDrawable dAsteroid = new ShapeDrawable(new PathShape(asteroidPath, 1, 1));
            dAsteroid.getPaint().setColor(Color.RED);
            dAsteroid.getPaint().setStyle(Paint.Style.STROKE);
            dAsteroid.setIntrinsicWidth(50);
            dAsteroid.setIntrinsicHeight(50);
            asteroidDrawable = dAsteroid;

            // Spaceship
            Path spaceshipPath = new Path();
            spaceshipPath.moveTo(0.0f, 0.0f);
            spaceshipPath.lineTo(1.0f, 0.5f);
            spaceshipPath.lineTo(0.0f, 1.0f);
            spaceshipPath.lineTo(0.0f, 0.0f);
            ShapeDrawable dSpaceship = new ShapeDrawable(new PathShape(spaceshipPath, 1, 1));
            dSpaceship.getPaint().setColor(Color.BLACK);
            dSpaceship.getPaint().setStyle(Paint.Style.STROKE);
            dSpaceship.setIntrinsicWidth(50);
            dSpaceship.setIntrinsicHeight(50);
            spaceshipDrawable = dSpaceship;

            // Missile
            ShapeDrawable dMissile = new ShapeDrawable(new RectShape());
            dMissile.getPaint().setColor(Color.BLACK);
            dMissile.getPaint().setStyle(Paint.Style.STROKE);
            dMissile.setIntrinsicWidth(15);
            dMissile.setIntrinsicHeight(3);
            missileDrawable = dMissile;

            // Background
            setBackgroundColor(Color.WHITE);

            // Graphic acceleration
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        } else {

            ////////////////////////////////////////////////////////// Bitmap mode

            // Asteroids
            asteroidDrawable = ctx.getResources().getDrawable(R.drawable.asteroid1);

            // Spaceship
            spaceshipDrawable = ctx.getResources().getDrawable(R.drawable.spaceship_pixel);

            // Missile
            missileDrawable = ctx.getResources().getDrawable(R.drawable.misile1);

            // Graphic acceleration
            setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }

        // Create Graphic instances and store them
        asteroids = new Vector<Graphic>();
        for(int i = 0; i < numAsteroids; i++) {
            Graphic asteroid = new Graphic(this, asteroidDrawable);
            asteroid.setVelY(Math.random() * 4 - 2);
            asteroid.setVelX(Math.random() * 4 - 2);
            asteroid.setAngle((int) (Math.random() * 360));
            asteroid.setRotation((int) (Math.random() * 8 - 4));
            asteroids.add(asteroid);
        }

        spaceship = new Graphic(this, spaceshipDrawable);

        missiles = new Vector<Graphic>();
        isMissileActive = new Vector<Boolean>();
        for(int i = 0; i < ammo; i++) {
            Graphic missile = new Graphic(this, missileDrawable);
            missiles.add(missile);
            isMissileActive.add(false);

            // Log.d("MISSILE:", missiles.elementAt(i).toString());
            // Log.d("IS M ACTIVE:", isMissileActive.elementAt(i).toString());
        }

        // Register sensors
        if (pref.getString("sensors", "0").equals("0")) {
            // None

        } else {
            // Some sensor enabled
            List<Sensor> sensorList;

            if (pref.getString("sensors", "0").equals("1")) {
                // ORIENTATION
                sensorList = mSensorManager.getSensorList(Sensor.TYPE_ORIENTATION);

            } else {
                // ACCELEROMETER
                sensorList = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);

            }
            if (!sensorList.isEmpty()) {
                Sensor orientationSensor = sensorList.get(0);
                mSensorManager.registerListener(this, orientationSensor, SensorManager.SENSOR_DELAY_GAME);
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        spaceship.setCentX(w / 2);
        spaceship.setCentY(h /2);

        for(Graphic asteroid : asteroids) {
            do {
                asteroid.setCentX((int) (Math.random() * w));
                asteroid.setCentY((int) (Math.random() * h));
            } while(asteroid.distance(spaceship) < (w + h)/5);
        }

        lastProcess = System.currentTimeMillis();
        //if (!thread.isAlive()) {
        thread.start();
        //}
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        spaceship.drawGraphic(canvas);

        for(Graphic asteroid : asteroids) {
            asteroid.drawGraphic(canvas);
        }

        for(int i = 0; i < missiles.size(); i++) {
            if(isMissileActive.elementAt(i) == Boolean.TRUE) {
                missiles.elementAt(i).drawGraphic(canvas);

            }
        }
    }


    // region Physics

    protected void updatePhysics() {
        long now = System.currentTimeMillis();
        if (lastProcess + PROCESS_PERIOD > now) {
            return;
        }

        double delay = (now - lastProcess) / PROCESS_PERIOD;
        lastProcess = now;

        // Spaceship
        spaceship.setAngle((int) (spaceship.getAngle() + spaceshipGyre * delay));
        double nVelX = spaceship.getVelX() + spaceshipAcceleration
                * Math.cos(Math.toRadians(spaceship.getAngle())) * delay;
        double nVelY = spaceship.getVelY() + spaceshipAcceleration
                * Math.sin(Math.toRadians(spaceship.getAngle())) * delay;

        if (Math.hypot(nVelX, nVelY) <= MAX_SPACECHIP_VEL) {
            spaceship.setVelX(nVelX);
            spaceship.setVelY(nVelY);
        }

        spaceship.incrementPosition(delay);

        // Asteroids
        for(Graphic asteroid : asteroids) {
            asteroid.incrementPosition(delay);
        }

        // Missiles
        for(int i = 0; i < missiles.size(); i++) {
            if (isMissileActive.elementAt(i) == Boolean.TRUE) {

                missiles.elementAt(i).incrementPosition(delay);

                for (int j = 0; j < asteroids.size(); j++) {
                    if (missiles.elementAt(i).checkCollision(asteroids.elementAt(j))) {

                        destroyAsteroid(j);
                        break;
                    }
                }
            }
        }

    }

    // endregion


    // region Shooting

    private void destroyAsteroid(int x) {
        asteroids.remove(x);
        isMissileActive.set(x, false);
    }

    private void shootMissile() {

        if (ammo == 0) {
            return;
        }

        isMissileActive.set(ammo-1, Boolean.TRUE);

        Graphic missile = missiles.elementAt(ammo - 1);
        ammo--;

        // Log.d("SHOOTING", ""+ammo);

        missile.setCentX(spaceship.getCentX());
        missile.setCentY(spaceship.getCentY());
        missile.setAngle(Math.cos(Math.toRadians(missile.getAngle())) * MISSILE_VEL);
        missile.setVelY(Math.sin(Math.toRadians(missile.getAngle())) * MISSILE_VEL);
    }

    // endregion


    // region Touch controls

    @Override
    public boolean onTouchEvent (MotionEvent event) {
        super.onTouchEvent(event);
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                shooting = true;
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(x - mX);
                float dy = Math.abs(y - mY);
                if (dy < 6 && dx > 6) {
                    spaceshipGyre = Math.round((x - mX) / 2);
                    shooting = false;
                } else if (dx < 6 && dy > 6) {

                    float acc = Math.round((mY - y) / 25);

                    // Forbid deceleration
                    if (acc > 0)
                    {
                        spaceshipAcceleration = acc;
                    }
                    shooting = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                spaceshipGyre = 0;
                spaceshipAcceleration = 0;
                if (shooting) {
                    shootMissile();
                }
                break;
        }

        mX = x;
        mY = y;
        return true;
    }

    // endregion


    // region SensorEventListener interface

    @Override
    public void onSensorChanged(SensorEvent event) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());

        if (pref.getString("sensors", "0").equals("1")) {
            // ORIENTATION
            float value = event.values[1];
            if(!orientationSensorHasInitialValue) {
                orientationSensorInitialValue = value;
                orientationSensorHasInitialValue = true;
            }
            spaceshipGyre = (int)(value - orientationSensorInitialValue)/3;

        } else {
            // ACCELEROMETER
            spaceshipGyre = (int)event.values[0];
            // Log.d("ACCELEROMETER", event.values[0] + "," + event.values[1] + "," + event.values[2]);

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    // endregion


    // region Threading
    class GameThread extends Thread {

        @Override
        public void run() {

            while (true) {
                updatePhysics();
            }
        }
    }

    // endregion
}
