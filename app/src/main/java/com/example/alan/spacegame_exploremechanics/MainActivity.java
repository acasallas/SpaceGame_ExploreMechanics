package com.example.alan.spacegame_exploremechanics;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity implements View.OnTouchListener {

    DrawingView drawingView;
    TextView fpsCounter;
    Button pauseResumeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
    }

    void initUI() {
        drawingView = (DrawingView) findViewById(R.id.drawing_view);
        fpsCounter = (TextView) findViewById(R.id.fps_counter);
        pauseResumeButton = (Button) findViewById(R.id.pause_resume_button);

        pauseResumeButton.setText("PAUSE");
        drawingView.setOnTouchListener(this);
    }

    public void pause_resume_game(View view) {
        if (drawingView.isGamePaused()) {
            drawingView.setGamePaused(false);
            pauseResumeButton.setText("PAUSE");
        } else {
            drawingView.setGamePaused(true);
            pauseResumeButton.setText("RESUME");
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        drawingView.processTouch(event);

        return true;
    }
}
