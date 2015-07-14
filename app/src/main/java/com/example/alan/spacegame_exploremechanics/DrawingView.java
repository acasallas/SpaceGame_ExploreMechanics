package com.example.alan.spacegame_exploremechanics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Alan on 7/10/2015.
 */
public class DrawingView extends SurfaceView implements SurfaceHolder.Callback {

    //Inner Thread class
    class DrawingThread extends Thread {

        private SurfaceHolder mSurfaceHolder;
        private static final long DRAW_PERIOD = 20L;
        private boolean stopFlag = false;
        private Paint mPaint;
        private Paint backgroundPaint;
        private long numFrames = 0;
        private long beginningOfTime = 0;
        private double fps = 0;
        private boolean isPaused = false;

        public boolean isPaused() {
            return isPaused;
        }

        public void setIsPaused(boolean isPaused) {
            this.isPaused = isPaused;
        }

        public DrawingThread(SurfaceHolder surfaceHolder) {
            mSurfaceHolder = surfaceHolder;

            mPaint = new Paint();
            mPaint.setTextSize(50);
            mPaint.setColor(Color.WHITE);

            backgroundPaint = new Paint();
            backgroundPaint.setColor(Color.BLACK);

            beginningOfTime = SystemClock.elapsedRealtime();
        }

        public double getFPS() {
            return fps;
        }


        public void stopThread() {
            stopFlag = true;
        }

        public boolean isStopped() {
            return stopFlag;
        }

        @Override
        public void run() {

            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

            while (!stopFlag) {
                if (!isPaused) {

                    long startTime = SystemClock.elapsedRealtime(); //get time at the start of thread loop

                    fps = (double) numFrames / (double) (startTime - beginningOfTime) * 1000L;
                    /**
                     * We use SurfaceHolder.lockCanvas() to get the canvas that draws to the SurfaceView.
                     * After we are done drawing, we let go of the canvas using SurfaceHolder.unlockCanvasAndPost()
                     * **/

                    engine.render();

                    Canvas c = null;
                    try {
                        c = mSurfaceHolder.lockCanvas();

                        if (c != null) {
                            synchronized (mSurfaceHolder) {

                                c.drawColor(Color.BLACK, PorterDuff.Mode.CLEAR);
                                //engine.erase(c);
                                c.drawText(String.format("FPS: %.3f", fps), 50, 50, mPaint);
                                engine.draw(c);
                            }
                        }
                    } finally {
                        if (c != null) {
                            mSurfaceHolder.unlockCanvasAndPost(c);
                        }
                    }

                    //send thread to sleep so we don't draw faster than the requested 'drawPeriod'.
                    long sleepTime = DRAW_PERIOD - (SystemClock.elapsedRealtime() - startTime);
                    try {
                        if (sleepTime > 0) {
                            this.sleep(sleepTime);
                        }
                    } catch (InterruptedException ex) {
                        Log.e(LOG_TAG, ex.getMessage());
                    }

                    numFrames += 1;

                    Log.e("space_game", "end loop");
                }
            }

            mSurfaceHolder = null;
        }
    }

    SurfaceHolder surfaceHolder;
    DrawingThread drawingThread;
    GameEngine engine;
    private static String LOG_TAG = "space_game";

    BallElement touchBall;
    boolean isDraggingBall = false;


    //three constructors required of any custom view
    public DrawingView(Context context) {
        super(context);
        initView();
    }
    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }
    public DrawingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    void initView() {
        surfaceHolder = getHolder(); //The SurfaceHolder object will be used by the thread to request canvas to draw on SurfaceView
        surfaceHolder.addCallback(this); //become a Listener to the three events below that SurfaceView throws
        drawingThread = new DrawingThread(surfaceHolder);
        engine = new GameEngine(DrawingThread.DRAW_PERIOD);

        touchBall = new BallElement();
        Paint paint3 = new Paint();
        paint3.setColor(Color.GREEN);
        touchBall.setPaint(paint3);
        touchBall.setPosition(400,400);
        touchBall.setSpeed(0,0,DrawingThread.DRAW_PERIOD);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (!drawingThread.isAlive()) {
            if (drawingThread.isStopped()) {
                drawingThread = new DrawingThread(surfaceHolder);
            }
            engine.setTouchBall(touchBall);
            engine.setWorldSize(width,height);
            drawingThread.start();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //command thread to stop, and wait until it stops
        boolean retry = true;
        drawingThread.stopThread();
        while (retry) {
            try {
                drawingThread.join();
                retry = false;
            } catch (InterruptedException e) {
                Log.e(LOG_TAG,e.getMessage());
            }
        }

    }

    public boolean isGamePaused() {
        return drawingThread.isPaused();
    }

    public void setGamePaused(boolean ricardo) {
        drawingThread.setIsPaused(ricardo);
    }

    public void processTouch(MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float xPos = event.getX();
                float yPos = event.getY();
                if (xPos > touchBall.getLeft() && xPos < touchBall.getRight() && yPos > touchBall.getTop() && yPos < touchBall.getBottom()) {
                    isDraggingBall = true;
                    dragBallIfDragging(xPos,yPos);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                dragBallIfDragging(event.getX(),event.getY());
                break;
            case MotionEvent.ACTION_UP:
                dragBallIfDragging(event.getX(),event.getY());
                isDraggingBall = false;
                break;
            case MotionEvent.ACTION_CANCEL:
                isDraggingBall = false;
                break;
        }
    }

    void dragBallIfDragging(float x, float y) {
        if (isDraggingBall) {
            touchBall.x = x;
            touchBall.y = y;
        }
    }
}
