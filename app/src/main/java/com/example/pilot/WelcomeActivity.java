package com.example.pilot;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class WelcomeActivity extends AppCompatActivity {

    private static String username;
    private static ArrayList<Integer> tapTestResults;
    Bundle currUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Intent in = getIntent();
//        currUser = in.getExtras();
//        username = currUser.getString("username");
//        tapTestResults = currUser.getIntegerArrayList(username+"Tap Test Results");

        if(savedInstanceState != null){

        }

    }

    /** Called when the user clicks the Tap Test */
    public void tapTest(View view) {
        Intent intent = new Intent(this, TapTestActivity.class);

        intent.putExtra("currUser",currUser);
        startActivity(intent);
    }

    /** Called when the user clicks the Spiral Test */
    public void spiralTest(View view) {
        Intent intent = new Intent(this, SpiralActivity.class);
        startActivity(intent);
    }

    /** Called when the user clicks the Leveler button */
    public void levelTest(View view) {
        Intent intent = new Intent(this, LevelActivity.class);
        startActivity(intent);
    }

//    @Override
//    public void onSaveInstanceState(Bundle savedInstanceState){
//        //saving the bundle for the particular user
//        savedInstanceState.putBundle(currUser.getString("username"), currUser);
//        super.onSaveInstanceState(savedInstanceState);
//    }

    public void bubbleTest(View view){
        Intent intent = new Intent(this, BubbleActivity.class);
        startActivity(intent);
    }

    public void armTest(View view) {
        Intent intent = new Intent(this, ArmActivity.class);
        startActivity(intent);
    }

    public void headTest(View v) {
        Intent intent = new Intent(this, HeadActivity.class);
        startActivity(intent);
    }
}
