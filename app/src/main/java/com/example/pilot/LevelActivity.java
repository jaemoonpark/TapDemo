package com.example.pilot;

import android.content.Context;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


/* Learning how to gather sensor data that senses movement of device */
public class LevelActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private float xDraw;
    private float yDraw;
    public BullseyeDrawView bullseyeView;
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
        float xAxis = event.values[0];
        float yAxis = event.values[1];

        //setting x draw coordinate
        xDraw = ((bullseyeView.getX() + bullseyeView.getWidth()) / 2) + (-xAxis * 10);

        //setting y draw coordinate
        yDraw = ((bullseyeView.getY() + bullseyeView.getHeight()) / 2) + (-yAxis * 10);

        System.out.println("xdraw: " + xDraw + " y draw: " + yDraw);
 //       bullseyeView.tracePath.moveTo(xDraw,yDraw);
        bullseyeView.tracePath.lineTo(xDraw,yDraw);
        bullseyeView.invalidate();
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

