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

import java.lang.Math;
import java.lang.reflect.Array;
import java.util.ArrayList;


/* Learning how to gather sensor data that senses movement of device */
public class LevelActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private float xDraw;
    private float yDraw;
    private boolean testStarted = false;
    private float Athreshold;
    private float Bthreshold;
    private float Cthreshold;
    private float Dthreshold;
    private String score;
    public BullseyeDrawView bullseyeView;
    private ArrayList<Double> distancesArray = new ArrayList<Double>();
    private TextView scoreView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        bullseyeView = (BullseyeDrawView) findViewById(R.id.bullseyedrawing);
        Athreshold = Resources.getSystem().getDisplayMetrics().widthPixels / 16;
        Bthreshold = Resources.getSystem().getDisplayMetrics().widthPixels / 8;
        Cthreshold = Resources.getSystem().getDisplayMetrics().widthPixels / 4;
        Dthreshold = Resources.getSystem().getDisplayMetrics().widthPixels / 2;
        scoreView = (TextView) findViewById(R.id.textView3);
        scoreView.setVisibility(View.GONE);

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

                   System.out.println("xdraw: " + xDraw + " y draw: " + yDraw);
            //       bullseyeView.tracePath.moveTo(xDraw,yDraw);
            System.out.println("x axis: " + xAxis + " y axis: " + yAxis);


//            bullseyeView.tracePath.moveTo(xDraw,yDraw);
            bullseyeView.tracePath.lineTo(xDraw, yDraw);
            bullseyeView.invalidate();
        }
    }



    private double getDistanceFromCenter(float x, float y){
        double xTemp = (double) x;
        double yTemp = (double) y;
        double xCenter = (bullseyeView.getX() + bullseyeView.getWidth()) / 2;
        double yCenter = (bullseyeView.getY() + bullseyeView.getHeight()) / 2;
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
        if(avg > Dthreshold){
            return "F";
        }
        else if(avg > Cthreshold && avg <= Dthreshold){
            return "D";
        }
        else if(avg > Bthreshold && avg <= Cthreshold){
            return "C";
        }
        else if(avg > Athreshold && avg <= Bthreshold){
            return "B";
        }
        else{
            return "A";
        }
    }

    public void startBullseyeTest(View view){
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
                    scoreView.setText("Score: " + finishScore);
                    scoreView.setVisibility(View.VISIBLE);

                }
            }.start();
        }

    }
}

