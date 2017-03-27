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

public class ArmActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private int tapCount = 3;
    private int curlCount = 0;
    private int curlAttempt = 0;
    private boolean attemptCurl = false;
    private boolean countCurls = false;
    private boolean initiateOnce = false;
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
        if(!countCurls && !initiateOnce) {
            tapCount -= 1;
            txtInstructions.setText("Make sure you're holding your phone/tablet in landscape mode. Ensure your arm is laid out comfortably on a flat surface. \nTo begin, tap " + tapCount + " times with the arm that is not being curled.");
            if (tapCount == 0) {
                countCurls = true;
                attemptCurl = true;
                initiateOnce = true;
                startTest = true;
                txtInstructions.setText("You have attempted 0 curls. You have completed 0 curls.");
            }
        }
    }
    @Override
    public final void onSensorChanged(SensorEvent event) {
        if(startTest) {
            float xAxis = event.values[0];
            float zAxis = event.values[2];
            if (countCurls) {

                //attempted arm curl
                if (attemptCurl && xAxis > 2) {
                    curlAttempt += 1;
                    txtInstructions.setText("You have attempted " + curlAttempt + " curls. You have completed " + curlCount + " curls.");
                    attemptCurl = false;
                }
                //attempted and did not finish
                if (!attemptCurl && xAxis < 0 && zAxis > 8) {
                    countCurls = false;
                    //end of test
                    if(curlAttempt == 10){
                        startTest = false;
                        txtInstructions.setText("Test is over. \nYou have attempted " + curlAttempt + " curls. You have completed " + curlCount + " curls.");
                    }
                }

                //a completed arm test
                //set x axis for 9  and zaxis -2 for a more strict 90 degree arm curl
                if (xAxis > 8 && zAxis < -1) {
                    curlCount += 1;
                    txtInstructions.setText("You have attempted " + curlAttempt + " curls. You have completed " + curlCount + " curls.");
                    countCurls = false;
                    //end of test
                    if(curlAttempt == 10){
                        startTest = false;
                        txtInstructions.setText("Test is over. \nYou have attempted " + curlAttempt + " curls. You have completed " + curlCount + " curls.");
                    }
                }
                System.out.println("X axis is " + xAxis);
                System.out.println("Z axis is " + zAxis);
                System.out.println("--------------------");
            } else {
                //indication arm got laid back down
                if (xAxis < 0 && zAxis > 8) {
                    countCurls = true;
                    attemptCurl = true;
                }
            }
        }

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
