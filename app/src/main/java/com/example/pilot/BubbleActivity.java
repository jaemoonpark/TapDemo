package com.example.pilot;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import org.w3c.dom.Text;

import cmsc436.tharri16.googlesheetshelper.CMSC436Sheet;

public class BubbleActivity extends AppCompatActivity implements CMSC436Sheet.Host{
    public static TextView textView;
    private CMSC436Sheet sheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bubble);
        textView = (TextView) findViewById(R.id.textView);



    }

    protected void removeIntro() {
        textView.setText("");
    }

    protected void sendResultToSheet(double leftHand, double rightHand){
        sheet = new CMSC436Sheet(this, getString(R.string.app_name), getString(R.string.CMSC436Sheet_spreadsheet_id));
        sheet.writeData(CMSC436Sheet.TestType.LH_POP, "t01p01", (float) leftHand);
        sheet.writeData(CMSC436Sheet.TestType.RH_POP, "t01p01", (float) rightHand);
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
