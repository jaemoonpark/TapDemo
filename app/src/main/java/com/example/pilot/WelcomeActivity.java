package com.example.pilot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    /** Called when the user clicks the Send button */
    public void startTest(View view) {
        Intent intent = new Intent(this, TapTestActivity.class);
        startActivity(intent);
    }
}
