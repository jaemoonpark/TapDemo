package com.example.pilot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

public class WalkActivity extends AppCompatActivity {
    //for timer
    protected long startTime = 0;
    protected long stopTime = 0;
    protected double time = 0.00;
    protected boolean started;

    private Button startWalkingButton;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk);

        startWalkingButton = (Button) findViewById(R.id.start_button);
        text = (TextView) findViewById(R.id.textView4);
        text.setVisibility(View.GONE);
        started = false;
    }

    public void walkTest(View view){
        if(!started) {
            started = true;
            //on start test
            startTime = System.currentTimeMillis();
            System.out.println(startTime);
        } else {
            started = false;
            //on stop test
            stopTime = System.currentTimeMillis();
            time = (stopTime - startTime);
            time = time / 1000;
            double speed = 25 / time;
            DecimalFormat df = new DecimalFormat("#.00");
            String speedRound = df.format(speed);

            startWalkingButton.setVisibility(View.GONE);
            text.setText("You walked at a speed of " + speedRound + " steps/seconds.");
            text.setVisibility(View.VISIBLE);
        }




    }
}
