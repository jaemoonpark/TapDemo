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
    public boolean canTapScreen = true;
    public Integer leftHand = 0;
    public Integer rightHand = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tap_test);
    }

    public void countTap(View view) {
        if (canTapScreen) {
            final TextView textViewToChange = (TextView) findViewById(R.id.txtInstruction);
            if (!startTest) {
                startTest = true;

                new CountDownTimer(10000, 1000) {
                    public void onTick(long pie) {
                    }

                    public void onFinish() {
                        if (leftHandTest) {
                            textViewToChange.setText("Get ready for your right hand!");
                            canTapScreen = false;
                            leftHandTest = false;


                            new CountDownTimer(3000, 1000) {
                                public void onTick(long pie) {
                                }
                                public void onFinish(){
                                    canTapScreen = true;
                                }
                            }.start();

                        } else {
                            textViewToChange.setText("Results: \n Left Hand: " + leftHand.toString() + "\n Right Hand: " + rightHand.toString());
                        }
                        startTest = false;
                    }

                }.start();

            }

            if (startTest) {
                if (leftHandTest) {
                    leftHand++;
                    textViewToChange.setText("Taps so far: " + leftHand.toString());
                } else {
                    rightHand++;
                    textViewToChange.setText("Taps so far: " + rightHand.toString());
                }

            }

        }
    }
}