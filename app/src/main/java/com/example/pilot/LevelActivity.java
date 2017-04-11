package com.example.pilot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import cmsc436.tharri16.googlesheetshelper.CMSC436Sheet;


/* Learning how to gather sensor data that senses movement of device */
public class LevelActivity extends AppCompatActivity implements SensorEventListener, CMSC436Sheet.Host {
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private float xDraw;
    private float yDraw;
    private boolean testStarted = false;
    private float Athreshold;
    private float Bthreshold;
    private float Cthreshold;
    private float Dthreshold;
    private double avg;
    private boolean rightHand = true;
    private String score;
    public BullseyeDrawView bullseyeView;
    private ArrayList<Double> distancesArray = new ArrayList<Double>();
    private CMSC436Sheet sheet;
    private int didFinish = 0;

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


//            bullseyeView.tracePath.moveTo(xDraw,yDraw);
            bullseyeView.tracePath.lineTo(xDraw, yDraw);
            bullseyeView.invalidate();
            bullseyeView.setPaintColor();
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
        avg = sum/distancesArray.size();
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
        if(didFinish !=2) {
            float xCenter = (bullseyeView.getX() + bullseyeView.getWidth()) / 2;
            float yCenter = (bullseyeView.getY() + bullseyeView.getHeight()) / 2;


            if (!testStarted) {
                testStarted = true;
                bullseyeView.tracePath.moveTo(xCenter, yCenter);
                new CountDownTimer(10000, 500) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        double distance = getDistanceFromCenter(xDraw, yDraw);
                        distancesArray.add(distance);

                    }

                    @Override
                    public void onFinish() {
                        testStarted = false;
                        String finishScore = getScore(distancesArray);
                        System.out.println(finishScore);

                        scoreView.setText("Score: " + finishScore);
                        sendResultToSheet();
                        didFinish++;
                        scoreView.setVisibility(View.VISIBLE);
                        bullseyeView.resetCanvas();
                    }
                }.start();
            }
        }

    }


    private void sendResultToSheet(){
        sheet = new CMSC436Sheet(this, getString(R.string.app_name), getString(R.string.CMSC436Sheet_spreadsheet_id));
        if(rightHand){
            sheet.writeData(CMSC436Sheet.TestType.RH_LEVEL, "t01p01", (float) avg);
            rightHand = false;
        }else{
            sheet.writeData(CMSC436Sheet.TestType.LH_LEVEL, "t01p01", (float) avg);
        }
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
