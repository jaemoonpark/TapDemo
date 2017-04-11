package com.example.pilot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import cmsc436.tharri16.googlesheetshelper.CMSC436Sheet;

public class ArmActivity extends AppCompatActivity implements SensorEventListener, CMSC436Sheet.Host {
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private int tapCount = 3;
    private int curlCount = 0;
    private int curlAttempt = 0;
    protected long startTime = 0;
    protected long stopTime = 0;
    protected double time = 0.00;
    private boolean attemptCurl = false;
    private boolean countCurls = false;
    private boolean initiateOnce = false;
    private boolean startTest = false;
    boolean getInitialValues;
    private TextView txtInstructions;
    private CMSC436Sheet sheet;


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
                startTime = System.currentTimeMillis();
            }
        }
    }
    @Override
    public final void onSensorChanged(SensorEvent event) {
        if(startTest) {
            float xAxis = Math.abs(event.values[0]);
            float zAxis = Math.abs(event.values[2]);
            if (countCurls) {
                System.out.println("RIGHT NOW TRYING TO MEASURE A COMPLETE CURL");
                //attempted arm curl
                if (attemptCurl && xAxis > 4) {
                    curlAttempt += 1;
                    txtInstructions.setText("You have attempted " + curlAttempt + " curls. You have completed " + curlCount + " curls.");
                    attemptCurl = false;
                }
                //attempted and did not finish
                if (!attemptCurl && xAxis < .2) {
                    System.out.println("FAILED");
                    attemptCurl= true;
                    //end of test
                    if(curlAttempt == 10){
                        startTest = false;
                        stopTime = System.currentTimeMillis();
                        time = (stopTime - startTime);
                        time = time /1000;
                        txtInstructions.setText("Test is over. \nYou have attempted " + curlAttempt + " curls. You have completed " + curlCount + " curls." + " It took you " + Double.toString(time) + " seconds");
                    }
                }

                //a completed arm test
                //set x axis for 9  and zaxis -2 for a more strict 90 degree arm curl
                if (xAxis > 8.5 && zAxis < 1.5) {
                    curlCount += 1;
                    txtInstructions.setText("You have attempted " + curlAttempt + " curls. You have completed " + curlCount + " curls.");
                    countCurls = false;
                    //end of test
                    if(curlAttempt == 10){
                        startTest = false;
                        stopTime = System.currentTimeMillis();
                        time = (stopTime - startTime);
                        time = time /1000;
                        txtInstructions.setText("Test is over. \nYou have attempted " + curlAttempt + " curls. You have completed " + curlCount + " curls." + " It took you " + Double.toString(time) + " seconds");
                        sendResultToSheet(curlAttempt, curlCount);
                    }
                }

            } else {
                //indication arm got laid back down
                System.out.println("COMPLETED CURL SECTION");
                if (xAxis < .4) {
                    countCurls = true;
                    attemptCurl = true;
                }
            }
            System.out.println("X axis is " + xAxis);
            System.out.println("Z axis is " + zAxis);
            System.out.println("--------------------");
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

    private void sendResultToSheet(double leftHand, double rightHand){
        sheet = new CMSC436Sheet(this, getString(R.string.app_name), getString(R.string.CMSC436Sheet_spreadsheet_id));
        sheet.writeData(CMSC436Sheet.TestType.LH_CURL, "t01p01", (float) leftHand);
        sheet.writeData(CMSC436Sheet.TestType.RH_CURL, "t01p01", (float) rightHand);
    }
    /* neccessary? */
    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        sheet.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        sheet.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public int getRequestCode(CMSC436Sheet.Action action) {
        switch (action) {
            case REQUEST_ACCOUNT_NAME:
                return 2;
            case REQUEST_AUTHORIZATION:
                return 2;
            case REQUEST_PERMISSIONS:
                return 2;
            case REQUEST_PLAY_SERVICES:
                return 2;
            default:
                return -1; // boo java doesn't know we exhausted the enum
        }
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void notifyFinished(Exception e) {
        if (e != null) {
            throw new RuntimeException(e); // just to see the exception easily in logcat
        }

        Log.i(getClass().getSimpleName(), "Done");
    }
}
