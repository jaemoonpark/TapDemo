package com.example.pilot;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

//Using this as a guide for the drawing functionality: https://code.tutsplus.com/tutorials/android-sdk-create-a-drawing-app-touch-interaction--mobile-19202

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


    //Called when SpiralView is assigned a size
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w,h,oldw,oldh);

        canvasBitmap = Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    protected void onDraw(Canvas canvas){
        canvas.drawBitmap(canvasBitmap, 0,0, canvasPaint);
        canvas.drawPath(tracePath, paint);
    }

    public boolean onTouchEvent(MotionEvent event){
        float touchX = event.getX();
        float touchY = event.getY();


        switch(event.getAction()){
            //Captures what happens when your finger MOVES across the screen
            case MotionEvent.ACTION_MOVE:
                tracePath.lineTo(touchX,touchY);
                break;

            //Captures what happens when your finger is pressed DOWN on the screen
            case MotionEvent.ACTION_DOWN:
                tracePath.moveTo(touchX,touchY);
                break;

            //Captures what happens when your finger is lifted UP from the screen
            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(tracePath,paint);
                tracePath.reset();
                break;
            default:
                return false;
        }

        //Completes the drawing
        invalidate();

        
        return true;
    }
}
