package com.example.alan.spacegame_exploremechanics;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Alan on 7/11/2015.
 */
public class GameEngine {

    BallElement ballElement;
    BallElement ballElement2;
    BallElement touchBall;

    int worldWidth;
    int worldHeight;

    final long PERIOD;

    public GameEngine(long period) {
        PERIOD = period;

        ballElement = new BallElement();
        Paint paint1 = new Paint();
        paint1.setColor(Color.BLUE);
        ballElement.setPaint(paint1);
        ballElement.setPosition(300, 300);
        ballElement.setSpeed(500, 600, period);

        ballElement2 = new BallElement();
        Paint paint2 = new Paint();
        paint2.setColor(Color.RED);
        ballElement2.setPaint(paint2);
        ballElement2.setPosition(600, 600);
        ballElement2.setSpeed(300, 750, period);


    }

    public void setTouchBall(BallElement ball) {
        touchBall = ball;
    }

    public void render() {

        boundaryCollision(ballElement);
        boundaryCollision(ballElement2);

        setNewPosition(ballElement);
        setNewPosition(ballElement2);
    }

    public void setWorldSize(int w, int h) {
        worldWidth = w;
        worldHeight = h;
    }
    
    public void boundaryCollision(BallElement ball) {
        //boundary conditions
        if (ball.getLeft() <= 0) {
            ball.setXSpeed(Math.abs(ball.getXSpeed()), PERIOD);
        } else if (ball.getRight() >= worldWidth) {
            ball.setXSpeed(-Math.abs(ball.getXSpeed()),PERIOD);
        }

        if (ball.getTop() <= 0) {
            ball.setYSpeed(Math.abs(ball.getYSpeed()), PERIOD);
        } else if (ball.getBottom() > worldHeight) {
            ball.setYSpeed(-Math.abs(ball.getYSpeed()), PERIOD);
        }
    }

    public void setNewPosition(BallElement ball) {
        ball.x += ball.dxFrame;
        ball.y += ball.dyFrame;
    }

    public void draw(Canvas c) {
        c.drawCircle(ballElement.x, ballElement.y, ballElement.radius, ballElement.getPaint());
        c.drawCircle(ballElement2.x, ballElement2.y, ballElement2.radius, ballElement2.getPaint());
        c.drawCircle(touchBall.x,touchBall.y,touchBall.radius,touchBall.getPaint());
    }

    void collide(BallElement elem1, BallElement elem2) {
        PointF newBasis = PhysicsUtil.toNorm(new PointF(elem2.x-elem1.x, elem2.y - elem1.y);
        
    }

    void performCollisionDetection() {

    }






}
