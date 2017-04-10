package com.example.pilot;

import android.content.Context;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class HeadActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private TextView instructions;
    private Button button;

    private boolean getMidpoint;
    private float midX;
    private float yAxisCenter;

    private float xDraw;
    private boolean testStarted = false;
    private float aThreshold;
    private float bThreshold;
    private float cThreshold;
    private float dThreshold;
    private String score;
    public HeadView headView;
    private ArrayList<Double> distancesArray = new ArrayList<Double>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        instructions = (TextView) findViewById(R.id.textbox);
        button = (Button) findViewById(R.id.button7);
        headView = (HeadView) findViewById(R.id.headDraw);

        aThreshold = Resources.getSystem().getDisplayMetrics().widthPixels / 16;
        bThreshold = Resources.getSystem().getDisplayMetrics().widthPixels / 8;
        cThreshold = Resources.getSystem().getDisplayMetrics().widthPixels / 4;
        dThreshold = Resources.getSystem().getDisplayMetrics().widthPixels / 2;
        getMidpoint = true;


    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        if(testStarted) {
            float xAxis = event.values[0];
            if(getMidpoint) {
                midX = xAxis;
                getMidpoint = false;
            }

            //setting x draw coordinate
            xDraw = ((headView.getX() + headView.getWidth()) / 2) + (-xAxis * (Resources.getSystem().getDisplayMetrics().widthPixels / 15));



           headView.tracePath.lineTo(xDraw, yAxisCenter);
            headView.invalidate();
        }
    }



    private double getDistanceFromCenter(float x, float y){
        double xTemp = (double) x;
        double yTemp = (double) y;
        return Math.sqrt(Math.pow(midX - xTemp,2) + Math.pow(yAxisCenter - yTemp,2));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_STATUS_ACCURACY_HIGH);
    }


    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    private String getScore(ArrayList<Double> distancesArray){
        double sum = 0;
        for(int i = 0; i < distancesArray.size(); i++){
            sum += distancesArray.get(i);
        }
        double avg = sum/distancesArray.size();
        if(avg > dThreshold){
            return "F";
        }
        else if(avg > cThreshold && avg <= dThreshold){
            return "D";
        }
        else if(avg > bThreshold && avg <= cThreshold){
            return "C";
        }
        else if(avg > aThreshold && avg <= bThreshold){
            return "B";
        }
        else{
            return "A";
        }
    }

    public void startHeadTest(View view){
        float xCenter = (headView.getX() + headView.getWidth()) / 2;
        yAxisCenter = (headView.getY() + headView.getHeight()) / 2;

        if(!testStarted){
            testStarted = true;
            headView.tracePath.moveTo(xCenter, yAxisCenter);
            instructions.setVisibility(View.GONE);
            button.setVisibility(View.GONE);

            new CountDownTimer(10000, 500){
                @Override
                public void onTick(long millisUntilFinished){
                    double distance = getDistanceFromCenter(xDraw, yAxisCenter);
                    distancesArray.add(distance);

                }

                @Override
                public void onFinish() {
                    testStarted = false;
                    score = getScore(distancesArray);
                    System.out.println(score);

                }
            }.start();
        }

    }
}
