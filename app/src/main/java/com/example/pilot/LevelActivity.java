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
import java.lang.Math;
import java.util.ArrayList;


/* Learning how to gather sensor data that senses movement of device */
public class LevelActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private float xDraw;
    private float yDraw;
    private boolean testStarted = false;
    public BullseyeDrawView bullseyeView;
    private ArrayList<Double> distancesArray = new ArrayList<Double>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        bullseyeView = (BullseyeDrawView) findViewById(R.id.bullseyedrawing);
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        // The light sensor returns a single value.
        // Many sensors return 3 values, one for each axis.
        if(testStarted) {
            float xAxis = event.values[0];
            float yAxis = event.values[1];

            //setting x draw coordinate
            xDraw = ((bullseyeView.getX() + bullseyeView.getWidth()) / 2) + (-xAxis * (Resources.getSystem().getDisplayMetrics().widthPixels / 15));

            //setting y draw coordinate
            yDraw = ((bullseyeView.getY() + bullseyeView.getHeight()) / 2) + (yAxis * (Resources.getSystem().getDisplayMetrics().heightPixels / 15));

            //       System.out.println("xdraw: " + xDraw + " y draw: " + yDraw);
            //       bullseyeView.tracePath.moveTo(xDraw,yDraw);
            System.out.println("x axis: " + xAxis + " y axis: " + yAxis);
            bullseyeView.tracePath.lineTo(xDraw, yDraw);
            bullseyeView.invalidate();
        }
    }


    private final double getD(float x, float y){
        float squareX = x * x;
        float squareY = y + y;
        float sum = squareX + squareY;
        Float f = sum;
        Double dubSum = new Double(f.toString());
        return Math.sqrt(dubSum);
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

    public void startBullseyeTest(View view){
        if(!testStarted){
            testStarted = true;
            new CountDownTimer(10000, 500){
                @Override
                public void onTick(long millisUntilFinished){
                   double distance = getD(xDraw, yDraw);

                }

                @Override
                public void onFinish() {
                    testStarted = false;
                }
            }.start();
        }

    }
}

