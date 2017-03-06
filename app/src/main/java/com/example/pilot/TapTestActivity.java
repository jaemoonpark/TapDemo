package com.example.pilot;

import android.Manifest;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class TapTestActivity extends AppCompatActivity {

    public boolean startTest = false;
    public boolean leftHandTest = true;
    public boolean canTapScreen = true;
    public Integer leftHand = 0;
    public Integer rightHand = 0;
    public Integer timeCount = 10;

    Bundle currUser;
    ArrayList<Integer> tapTestResults;
    String dominance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tap_test);

        Intent in = getIntent();
        currUser = in.getExtras();
        tapTestResults = currUser.getIntegerArrayList(currUser.getString("username"));
        dominance = currUser.getString(currUser.getString("username")+"dominantHand");
    }

    public void countTap(View view) {
        if (canTapScreen) {
            final TextView textViewToChange = (TextView) findViewById(R.id.txtInstruction);
            final TextView textViewToChange2 = (TextView) findViewById(R.id.txtTime);
            final Button btn = (Button) findViewById(R.id.tapTestComplete);
            if (!startTest) {
                startTest = true;
                new CountDownTimer(10000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        timeCount--;
                        textViewToChange2.setText(timeCount.toString());
                    }

                    public void onFinish() {
                        if (leftHandTest) {
                            textViewToChange.setText("Get ready for your right hand!");
                            textViewToChange2.setText("10");
                            timeCount = 10;
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
                            int rightHandScore = getScore("Right");
                            int leftHandScore = getScore("Left");
                            int averageScore  = getScore("Average");

                            System.out.println("Right Hand Score : " + rightHandScore);
                            System.out.println("Left Hand Score : " + leftHandScore);
                            System.out.println("Average Hand Score : " + averageScore);
                            textViewToChange.setText("Results: \n Left Hand: " + leftHand.toString() + "\n Right Hand: " + rightHand.toString());
                            textViewToChange2.setText("Done");
                            btn.setVisibility(View.VISIBLE);
                            canTapScreen = false;
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

    public void bringMeHome(){
        tapTestResults.add(leftHand);
        tapTestResults.add(rightHand);
        currUser.putIntegerArrayList(currUser.getString("username")+"Tap Test Results", tapTestResults);

        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.putExtras(currUser);

        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBundle(currUser.getString("username"), currUser);
        super.onSaveInstanceState(savedInstanceState);
    }

    public int getScore(String type) {
        if (type.equals("Right")) {
            if(rightHand <= 20) {
                return 1;
            } else if (20 < rightHand && rightHand < 30) {
                return 2;
            } else if (30 < rightHand && rightHand < 40) {
                return 3;
            } else if (40 < rightHand && rightHand < 50) {
                return 4;
            } else if (rightHand > 50) {
                return 5;
            }
        } else if (type.equals("Left")) {
            if(leftHand <= 20) {
                return 1;
            } else if (20 < leftHand && leftHand < 30) {
                return 2;
            } else if (30 < leftHand && leftHand < 40) {
                return 3;
            } else if (40 < leftHand && leftHand < 50) {
                return 4;
            } else if (leftHand > 50) {
                return 5;
            }
        } else if (type.equals("Average")) {
            int avg = (leftHand + rightHand) / 2;

            if(avg <= 20) {
                return 1;
            } else if (20 < avg && avg < 30) {
                return 2;
            } else if (30 < avg && avg < 40) {
                return 3;
            } else if (40 < avg && avg < 50) {
                return 4;
            } else if (avg > 50) {
                return 5;
            }
        }
        return -1;
    }
}