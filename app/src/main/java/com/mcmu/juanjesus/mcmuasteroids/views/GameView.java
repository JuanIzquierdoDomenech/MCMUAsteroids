package com.mcmu.juanjesus.mcmuasteroids.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
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

        spaceshipDrawable = ctx.getResources().getDrawable(R.drawable.spaceship);
        asteroidDrawable = ctx.getResources().getDrawable(R.drawable.asteroid1);

        spaceship = new Graphic(this, spaceshipDrawable);

        asteroids = new Vector<Graphic>();
        for(int i = 0; i < numAsteroids; i++) {
            Graphic asteroid = new Graphic(this, asteroidDrawable);
            asteroid.setVelY(Math.random() * 4 - 2);
            asteroid.setVelX(Math.random() * 4 - 2);
            asteroid.setAngle((int) (Math.random() * 360));
            asteroid.setRotation((int) (Math.random() * 8 - 4));
            asteroids.add(asteroid);
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
