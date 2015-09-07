package com.mcmu.juanjesus.mcmuasteroids.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.graphics.drawable.shapes.Shape;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.mcmu.juanjesus.mcmuasteroids.R;
import com.mcmu.juanjesus.mcmuasteroids.graphics.Graphic;

import java.util.Vector;

public class GameView extends View {

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

    //endregion


    public GameView(Context ctx, AttributeSet attrs) {

        super(ctx, attrs);

        Drawable spaceshipDrawable, asteroidDrawable, misileDrawable;

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

            // Graphic acceleration
            setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }

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
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        spaceship.drawGraphic(canvas);

        for(Graphic asteroid : asteroids) {
            asteroid.drawGraphic(canvas);
        }
    }
}
