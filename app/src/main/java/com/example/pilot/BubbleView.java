package com.example.pilot;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;



public class BubbleView extends View {
    Paint paint = null;

    public BubbleView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        paint = new Paint();
        paint.setColor(Color.GREEN);
    }

    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        int x = getWidth();
        int y = getHeight();
        int radius = 50;
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(x/2,y/2,radius,paint);
    }
}
