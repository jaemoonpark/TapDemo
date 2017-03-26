package com.example.pilot;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ArmActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private float initialX;
    private float initialY;
    private float verticalX;
    private float verticalY;
    private int complete; //when complete is 2 then that will be a full curl
    private int curlCount;
    boolean getInitialValues;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arm);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        getInitialValues = true;
        complete = 0;
        curlCount = 0;
    }

    public final void onSensorChanged(SensorEvent event) {
        if ( getInitialValues) {
            initialX = event.values[0];
            initialY = event.values[1];
            System.out.println(initialX);
            System.out.println(initialY);
            getInitialValues = false;
        }

        //if(x and y are in range of intitalX and Y then start the checking for veritcalX and Y)//
        //while(complete != 2) {
           // if(x and y axis is within range of veritcalX and veritcalY then complete = 1) //
            //if(x and y axis is within range of intitalX and Y then complete = 2 //

        //}

        complete = 0;
        curlCount++;

        float xAxis = event.values[0];
        float yAxis = event.values[1];

        /*compare the x and y axis to the initialX and initialY and if it is within a range mark
        that as a completed curl (need to figure out an error value)
         */


    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }


    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_STATUS_ACCURACY_HIGH);
    }
}
