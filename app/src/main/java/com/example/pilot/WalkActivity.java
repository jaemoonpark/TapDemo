package com.example.pilot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WalkActivity extends AppCompatActivity {
    //for timer
    protected long startTime = 0;
    protected long stopTime = 0;
    protected double time = 0.00;

    private Button startWalkingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk);

        startWalkingButton = (Button) findViewById(R.id.start_button);
    }

    public void walkTest(View view){

        //on start test
        startTime = System.currentTimeMillis();

        //on stop test
        stopTime = System.currentTimeMillis();
        time = (stopTime - startTime);
        time = time / 1000;
    }
}
