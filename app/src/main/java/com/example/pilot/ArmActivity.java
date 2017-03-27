package com.example.pilot;

import android.content.Context;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ArmActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private int tapCount = 3;
    private boolean startTest = false;
    boolean getInitialValues;
    private TextView txtInstructions;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("spaghetti");
        setContentView(R.layout.activity_arm);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        getInitialValues = true;
        txtInstructions = (TextView) findViewById(R.id.txtArmInstruction);
    }

    public void startTest(View view){
        if(!startTest) {
            tapCount -= 1;
            txtInstructions.setText("Make sure you're holding your phone/tablet in landscape mode. Ensure your arm is laid out comfortably on a flat surface. \nTo begin, tap " + tapCount + " times with the arm that is not being curled.");
            if (tapCount == 0) {
                startTest = true;
                txtInstructions.setText("");
            }
        }
    }
    @Override
    public final void onSensorChanged(SensorEvent event) {
        // The light sensor returns a single value.
        // Many sensors return 3 values, one for each axis.
            float xAxis = event.values[0];
            float yAxis = event.values[1];
            float zAxis = event.values[2];
        //debug garlic bread
        System.out.println("X axis is " + xAxis);
        System.out.println("Y axis is " + yAxis);
        System.out.println("Z axis is " + zAxis);
        System.out.println("--------------------");
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

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

}
