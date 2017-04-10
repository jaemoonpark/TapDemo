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
import android.widget.TextView;

import java.util.ArrayList;

public class HeadActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private TextView instructions;

    private boolean getMidpoint;
    private float midX;
    private float midY;

    private float xDraw;
    private float yDraw;
    private boolean testStarted = false;
    private float aThreshold;
    private float bThreshold;
    private float cThreshold;
    private float dThreshold;
    private String score;
    public HeadView headView;
    private ArrayList<Double> distancesArray = new ArrayList<Double>();
//    private TextView scoreView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        instructions.append("\n For this test you will place the phone on top on your head and it will record your sway. \n" +
                "When you start place the phone on your head and when you hear the beep stay as still as possible until you hear a second beep.\n" +
                "When you hit ok, you will have five seconds to place the phone on your head before the test starts");
        headView = (HeadView) findViewById(R.id.headDraw);
        aThreshold = Resources.getSystem().getDisplayMetrics().widthPixels / 16;
        bThreshold = Resources.getSystem().getDisplayMetrics().widthPixels / 8;
        cThreshold = Resources.getSystem().getDisplayMetrics().widthPixels / 4;
        dThreshold = Resources.getSystem().getDisplayMetrics().widthPixels / 2;
        getMidpoint = true;
//        scoreView = (TextView) findViewById(R.id.textView3);
//        scoreView.setVisibility(View.GONE);

    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        if(testStarted) {
            float xAxis = event.values[0];
            float yAxis = event.values[1];
            if(getMidpoint) {
                midX = xAxis;
                midY = yAxis;
            }

            //setting x draw coordinate
            xDraw = ((headView.getX() + headView.getWidth()) / 2) + (-xAxis * (Resources.getSystem().getDisplayMetrics().widthPixels / 15));

            //setting y draw coordinate
            yDraw = ((headView.getY() + headView.getHeight()) / 2) + (yAxis * (Resources.getSystem().getDisplayMetrics().heightPixels / 15));


            System.out.println("x axis: " + xAxis + " y axis: " + yAxis);


            headView.tracePath.lineTo(xDraw, yDraw);
            headView.invalidate();
        }
    }



    private double getDistanceFromCenter(float x, float y){
        double xTemp = (double) x;
        double yTemp = (double) y;
        double xCenter = (headView.getX() + headView.getWidth()) / 2;
        double yCenter = (headView.getY() + headView.getHeight()) / 2;
        return Math.sqrt(Math.pow(xCenter - xTemp,2) + Math.pow(yCenter - yTemp,2));
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
        if(!testStarted){
            testStarted = true;
            new CountDownTimer(10000, 500){
                @Override
                public void onTick(long millisUntilFinished){
                    double distance = getDistanceFromCenter(xDraw, yDraw);
                    distancesArray.add(distance);

                }

                @Override
                public void onFinish() {
                    testStarted = false;
                    String finishScore = getScore(distancesArray);
                    System.out.println(finishScore);
//                    scoreView.setText("Score: " + finishScore);
//                    scoreView.setVisibility(View.VISIBLE);

                }
            }.start();
        }

    }
}
