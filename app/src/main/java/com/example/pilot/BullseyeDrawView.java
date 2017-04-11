package com.example.pilot;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;

//Using this as a guide for the drawing functionality: https://code.tutsplus.com/tutorials/android-sdk-create-a-drawing-app-touch-interaction--mobile-19202

public class BullseyeDrawView extends View {

    protected Path tracePath;
    protected Paint paint;
    protected Paint canvasPaint;
    protected static final int RED= 0xff660000;
    protected Canvas drawCanvas;
    protected Bitmap canvasBitmap;
    protected long startTime = 0;
    protected long stopTime = 0;
    protected double time = 0.00;

    public BullseyeDrawView(Context context, AttributeSet attributes) {
        super(context, attributes);
        drawPrep();
    }

    protected void drawPrep(){
        //Create new path that will follow the finger as it traces the spiral

        tracePath = new Path();

        //The actual paint that will be on the screen
        paint = new Paint();
        paint.setColor(Color.TRANSPARENT);

        //These will make drawing smoother
        paint.setAntiAlias(true);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);

        //Sets width of brush and stroke instead of fill
        paint.setStrokeWidth(25);
        paint.setStyle(Paint.Style.STROKE);

        //According to the doc: Dithering affects how colors that are higher precision
        // than the device are down-sampled
        canvasPaint = new Paint(Paint.DITHER_FLAG);

//        double x = getWidth() / 2;
//        double y = getHeight() / 2;
//
//        tracePath.moveTo((float) x, (float) y);
//        tracePath.lineTo((float) x, (float) y);


    }

    //Called when BullseyeDrawView is assigned a size
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w,h,oldw,oldh);
        canvasBitmap = Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    protected void onDraw(Canvas canvas){
        canvas.drawBitmap(canvasBitmap, 0,0, canvasPaint);

        // resets the path to middle to remove weird intialization bug
//        double x = getWidth() / 2;
//        double y = getHeight() / 2;
//
//        tracePath.moveTo((float) x, (float) y);


        canvas.drawPath(tracePath, paint);
    }

    public void resetCanvas(){
        paint.setColor(Color.BLUE);

    }

    public void setPaintColor() {
        paint.setColor(Color.GREEN);
    }


}