package com.mcmu.juanjesus.mcmuasteroids.graphics;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

public class Graphic {

    private Drawable drawable;
    private View view;
    private int centX, centY;
    private int prevX, prevY;
    private int height, width;
    private double velX, velY;
    private double angle, rotation;
    private int collisionRadius;
    private int invalRadius;

    public Graphic(View v, Drawable d) {

        this.view = v;
        this.drawable = d;
        width = drawable.getIntrinsicWidth();
        height = drawable.getIntrinsicHeight();
        collisionRadius = (height+width)/4;
        invalRadius = (int)Math.hypot(width/2, height/2);
    }

    public void drawGraphic(Canvas canvas) {
        int x = centX - width/2;
        int y = centY - height/2;
        drawable.setBounds(x, y, x+width, y+height);
        canvas.save();
        canvas.rotate((float)angle, centX, centY);
        drawable.draw(canvas);
        canvas.restore();
        view.invalidate(centX-invalRadius, centY-invalRadius, centX+invalRadius, centY+invalRadius);
        view.invalidate(prevX-invalRadius, prevY-invalRadius, prevX+invalRadius, prevY+invalRadius);
        prevX = x;
        prevY = y;
    }

    public void incrementPosition(double factor) {
        centX += velX * factor;
        centY += velY * factor;
        angle += rotation * factor;

        if(centX < 0) centX = view.getWidth();
        if(centX > view.getWidth()) centX = 0;
        if(centY < 0) centY = view.getHeight();
        if(centY > view.getHeight()) centY = 0;
    }

    public double distance(Graphic g) {
        return Math.hypot(centX - g.centX, centY - g.centY);
    }

    public boolean checkCollision(Graphic g) {
        return (distance(g) < (collisionRadius + g.collisionRadius));
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public int getCentX() {
        return centX;
    }

    public void setCentX(int centX) {
        this.centX = centX;
    }

    public int getCentY() {
        return centY;
    }

    public void setCentY(int centY) {
        this.centY = centY;
    }

    public int getPrevX() {
        return prevX;
    }

    public void setPrevX(int prevX) {
        this.prevX = prevX;
    }

    public int getPrevY() {
        return prevY;
    }

    public void setPrevY(int prevY) {
        this.prevY = prevY;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public double getVelX() {
        return velX;
    }

    public void setVelX(double velX) {
        this.velX = velX;
    }

    public double getVelY() {
        return velY;
    }

    public void setVelY(double velY) {
        this.velY = velY;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public int getCollisionRadius() {
        return collisionRadius;
    }

    public void setCollisionRadius(int collisionRadius) {
        this.collisionRadius = collisionRadius;
    }

    public int getInvalRadius() {
        return invalRadius;
    }

    public void setInvalRadius(int invalRadius) {
        this.invalRadius = invalRadius;
    }
}
