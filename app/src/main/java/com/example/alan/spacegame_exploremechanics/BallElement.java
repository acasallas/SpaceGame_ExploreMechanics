package com.example.alan.spacegame_exploremechanics;

import android.graphics.Paint;

/**
 * Created by Alan on 7/11/2015.
 */
public class BallElement {

    //pos and pos/sec
    private float dx = 0;
    private float dy = 0;
    public float x = 0;
    public float y = 0;

    //pos/frame
    public float dxFrame = 0;
    public float dyFrame = 0;

    public float radius = 100;

    public Paint getPaint() {
        return mPaint;
    }

    public void setPaint(Paint mPaint) {
        this.mPaint = mPaint;
    }

    public Paint mPaint;

    public void setSpeed(float x, float y, long period) {
        setXSpeed(x,period);
        setYSpeed(y,period);
    }

    public void setXSpeed(float x, long period) {
        this.dx = x;
        this.dxFrame = this.dx * ((float)period/1000f);
    }

    public void setYSpeed(float y, long period) {
        this.dy = y;
        this.dyFrame = this.dy * ((float)period/1000f);
    }

    public float getXSpeed() {
        return this.dx;
    }

    public float getYSpeed() {
        return this.dy;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getTop() {
        return y - radius;
    }

    public float getBottom() {
        return y + radius;
    }

    public float getLeft() {
        return x - radius;
    }

    public float getRight() {
        return x + radius;
    }

}
