package com.example.pilot;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;



public class BubbleView extends View {
    Paint paint = null;
    int num;
    int num2;
    int circleX;
    int circleY;

    public BubbleView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        paint = new Paint();
        paint.setColor(Color.GREEN);
        num = -1;
        num2 = -1;
    }

    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        int x = getWidth();
        int y = getHeight();

        circleX = x/2;
        circleY = y/2;
        int radius = 50;
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(circleX,circleY,radius,paint);

        if(isSwitched()) {
            paint.setColor(Color.RED);
            canvas.drawColor(Color.WHITE);
            canvas.drawCircle(circleX,circleY,radius,paint);
            num2 = num;
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        changeNum();
        invalidate();
        return true;
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
