package com.example.pilot;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SpiralActivity extends AppCompatActivity {
    private Path tracePath;
    private Paint paint;
    private Paint canvasPaint;
    private int redPaint = 0xff0000;
    private Canvas drawCanvas;
    private Bitmap canvasBitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spiral);
    }

    protected void drawPrep(){
        tracePath = new Path();
        paint = new Paint();
    }
}
