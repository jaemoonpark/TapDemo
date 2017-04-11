package com.example.pilot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import cmsc436.tharri16.googlesheetshelper.CMSC436Sheet;

public class HeadActivity extends AppCompatActivity implements SensorEventListener, CMSC436Sheet.Host {
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private TextView instructions;
    private Button button;

    private boolean getMidpoint;
    private float midX;
    private float yAxisCenter;

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

    private MediaPlayer startTest;
    private MediaPlayer testComplete;
    private CMSC436Sheet sheet;

    private int trials = 0;
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
            float yAxis = event.values[1];
            if(getMidpoint) {
                midX = xAxis;
                getMidpoint = false;
            }

            //setting x draw coordinate
            xDraw = ((headView.getX() + headView.getWidth()) / 2) + (-xAxis * (Resources.getSystem().getDisplayMetrics().widthPixels / 15));
            yDraw = ((headView.getY() + headView.getHeight()) / 2) + (yAxis * (Resources.getSystem().getDisplayMetrics().heightPixels / 15));



            headView.tracePath.lineTo(xDraw, yDraw);
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

        instructions.setVisibility(View.GONE);
        button.setVisibility(View.GONE);
        float xCenter = (headView.getX() + headView.getWidth()) / 2;
        yAxisCenter = (headView.getY() + headView.getHeight()) / 2;
        if (!testStarted) {
            testStarted = true;
            headView.tracePath.moveTo(xCenter, yAxisCenter);
            //Play start test sound byte
            playStart();
            new CountDownTimer(10000, 500) {
                @Override
                public void onTick(long millisUntilFinished) {
                    double distance = getDistanceFromCenter(xDraw, yDraw);
                    distancesArray.add(distance);
                }
                @Override
                public void onFinish() {
                    //Play test complete sound
                    playEnd();
                    testStarted = false;
                    score = getScore(distancesArray);
                    System.out.println(score);
                    trials++;
                    Thread.yield();
                    try{
                        Thread.sleep(5000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }

                }
            }.start();

        }
    }

    private void playEnd(){
        if(testComplete == null) {
            testComplete = MediaPlayer.create(this, R.raw.test_complete);
            testComplete.start();
            while (testComplete.isPlaying()) {

            }
            testComplete.reset();
            testComplete.release();
            testComplete = null;
        }
    }

    private void playStart(){
        if(startTest == null) {
            startTest = MediaPlayer.create(this, R.raw.start_test);
            startTest.start();
            while (startTest.isPlaying()) {

            }
            startTest.reset();
            startTest.release();
            startTest = null;
        }
    }
    private void sendResultToSheet(){
        //there is not implementation for head yet
      ///  sheet = new CMSC436Sheet(this, getString(R.string.app_name), getString(R.string.CMSC436Sheet_spreadsheet_id));
      //  sheet.writeData(CMSC436Sheet.TestType., "t01p01", leftHand);
     //   sheet.writeData(CMSC436Sheet.TestType.RH_TAP, "t01p01", rightHand);
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
