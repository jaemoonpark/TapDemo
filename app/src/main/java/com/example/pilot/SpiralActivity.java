package com.example.pilot;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

public class SpiralActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SpiralView draw = new SpiralView(null, null);
        RelativeLayout lay = new RelativeLayout(this);

        lay.addView(draw);
        setContentView(R.layout.activity_spiral);
    }




}
