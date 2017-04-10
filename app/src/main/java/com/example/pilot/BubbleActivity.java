package com.example.pilot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

import org.w3c.dom.Text;

public class BubbleActivity extends AppCompatActivity {
    public static TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bubble);
        textView = (TextView) findViewById(R.id.textView);



    }

    protected void removeIntro() {
        textView.setText("");
    }


}
