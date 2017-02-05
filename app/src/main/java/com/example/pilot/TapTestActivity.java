package com.example.pilot;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class TapTestActivity extends AppCompatActivity {

    public boolean startTest = false;
    public boolean leftHandTest = true;
    public Integer leftHand = 0;
    public int rightHand = 0;
    //TextView textViewToChange;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tap_test);
    }

    public void countTap(View view) {
        final TextView textViewToChange = (TextView) findViewById(R.id.txtInstruction);
        if(!startTest){
            startTest = true;
            new CountDownTimer(10000,1000){

                public void onTick(long pie){

                }
                public void onFinish(){
                    textViewToChange.setText("stop");
                    startTest = false;
                }

            }.start();

        }

        if(startTest){
            leftHand++;
            textViewToChange.setText(leftHand.toString());
        }
    }
}
