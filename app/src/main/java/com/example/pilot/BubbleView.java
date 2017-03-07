package com.example.pilot;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.Random;


public class BubbleView extends View {
    Paint paint = null;
    int num;
    int num2;
    int circleX;
    int circleY;
    Random r = new Random();
    protected long startTime;
    protected long endTime;
    protected long averageTime = 0;
    int numTrials = 0;
    public BubbleView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        paint = new Paint();
        paint.setColor(Color.GREEN);
        num = -1;
        num2 = -1;

    }

    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        if(numTrials < 11) {
            int x = getWidth();
            int y = getHeight();

            circleX = x / 2;
            circleY = y / 2;
            int radius = 50;
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(circleX, circleY, radius, paint);
            startTime = System.currentTimeMillis();
            if (isSwitched()) {
                circleX = r.nextInt(x - 1);
                circleY = r.nextInt(y - 1);
                paint.setColor(Color.RED);
                canvas.drawColor(Color.WHITE);
                canvas.drawCircle(circleX, circleY, radius, paint);
                num2 = num;

            }
        }else{
            Log.v("avgTime", Double.toString(averageTime) + " seconds");
        }
    }

    private double getCurrentCircleX(){
        return circleX;
    }

    private double getCurrentCircleY(){
        return circleY;
    }

    private boolean isWithin(double circle, double touch){
        return Math.abs(circle - touch) <= 70;
    }

    public boolean onTouchEvent(MotionEvent event) {
        double touchX = event.getX();
        double touchY = event.getY();


        if(isWithin(getCurrentCircleX(), touchX) && isWithin(getCurrentCircleY(), touchY)) {

            numTrials++;
            changeNum();
            invalidate();
            endTime = System.currentTimeMillis();
            if (averageTime == 0) {
                averageTime = endTime;
            } else {
                averageTime = (averageTime + endTime) / 2;
            }
            return true;

        }else{
            return true;
        }
    }

    public boolean isSwitched() {
        System.out.println(num == num2);
        return num != num2;
    }

    public void changeNum() {
        if(num == -1 || num == 0) {
            num = 1;
            num2 = 0;
        } else {
            num = 0;
            num2 = 1;
        }
    }
}
