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
    public Integer rightHand = 0;
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
                    if(leftHandTest){
                        textViewToChange.setText("get ready 4 ur other hand");
                        leftHandTest = false;
                    }
                    else{
                        textViewToChange.setText("Results: \n Left Hand: " + leftHand.toString() + "\n Right Hand: " + rightHand.toString());
                    }
                    startTest = false;
                }

            }.start();

        }

        if(startTest){
            if(leftHandTest){
                leftHand++;
                textViewToChange.setText("Taps so far: " + leftHand.toString());
            }
            else{
                rightHand++;
                textViewToChange.setText("Taps so far: " + rightHand.toString());
            }

        }
    }
}
