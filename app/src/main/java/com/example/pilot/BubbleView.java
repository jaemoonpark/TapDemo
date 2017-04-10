package com.example.pilot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
    protected double averageTime = 0.0;
    int numTrials = 0;
    BubbleActivity host;

    public BubbleView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        paint = new Paint();
        paint.setColor(Color.GREEN);
        num = -1;
        num2 = -1;
        host = (BubbleActivity) getContext();

    }

    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        if(numTrials < 10) {
            int x = getWidth();
            int y = getHeight();

            circleX = x / 2;
            circleY = y / 2;
            int radius = 50;
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(circleX, circleY, radius, paint);

            if (isSwitched()) {
                circleX = r.nextInt(x - 1);
                circleY = r.nextInt(y - 1);
                paint.setColor(Color.RED);
                canvas.drawColor(Color.WHITE);
                canvas.drawCircle(circleX, circleY, radius, paint);
                num2 = num;
                startTime = System.currentTimeMillis();

            }
        }else{
            Log.v("avgTime", Double.toString(averageTime) + " seconds");
            BubbleActivity.textView.setText("Average reaction time: " + averageTime +" seconds");
            sendToSheets();

        }
    }

    private void sendToSheets() {

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
            double time = ((endTime-startTime)/1000.0);
            Log.v("time", Double.toString(time));

            if (time <180){
                averageTime = (averageTime + time) / 2;
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

        if(num == -1 ) {
            host.removeIntro();
            num = 1;
            num2 = 0;
        } else if( num == 0) {
            num = 1;
            num2 = 0;
        } else {
            num = 0;
            num2 = 1;
        }
    }
}
