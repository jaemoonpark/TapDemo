package com.example.pilot;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;



public class SpiralView extends View {

    protected Path tracePath;
    protected Paint paint;
    protected Paint canvasPaint;
    protected static final int RED= 0xff0000;
    protected Canvas drawCanvas;
    protected Bitmap canvasBitmap;

    public SpiralView(Context context, AttributeSet attributes) {
        super(context, attributes);
        drawPrep();
    }

    protected void drawPrep(){
        //Create new path that will follow the finger as it traces the spiral
        tracePath = new Path();
        //The actual paint that will be on the screen
        paint = new Paint();
        paint.setColor(RED);

        //These will make drawing smoother
        paint.setAntiAlias(true);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);

        //Sets width of brush and stroke instead of fill
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);

        //According to the doc: Dithering affects how colors that are higher precision
        // than the device are down-sampled
        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }
}
