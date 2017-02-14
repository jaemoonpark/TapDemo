package com.example.pilot;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;

//Using this as a guide for the drawing functionality: https://code.tutsplus.com/tutorials/android-sdk-create-a-drawing-app-touch-interaction--mobile-19202

public class SpiralView extends View {

    protected Path tracePath;
    protected Paint paint;
    protected Paint canvasPaint;
    protected static final int RED= 0xff660000;
    protected Canvas drawCanvas;
    protected Bitmap canvasBitmap;
    protected long startTime = 0;
    protected long stopTime = 0;
    protected double time = 0.00;

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
        paint.setStrokeWidth(25);
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
                startTime = System.currentTimeMillis();
                break;

            //Captures what happens when your finger is lifted UP from the screen
            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(tracePath,paint);
                tracePath.reset();
                //asking for permission to save
                //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                //saving screen here
                //loading view
                View spiralView = findViewById(R.id.drawing);
                spiralView.setDrawingCacheEnabled(true);
                Bitmap spiralViewBitMap = spiralView.getDrawingCache();
                File postSpiralImageFile = new File(Environment.getExternalStorageDirectory() + "/spiral/");
                postSpiralImageFile.mkdirs();
                File file = new File(postSpiralImageFile, "spiral" + Long.toString(System.currentTimeMillis()) + ".png");

                //this line of code simply saves directly to gallery
                MediaStore.Images.Media.insertImage(getContext().getContentResolver(), spiralViewBitMap,"spiral" + Long.toString(System.currentTimeMillis()) + ".png", "after spiral draw test");
                //creating new file here
                try
                {
                    //starting output stream using spiral image file
                    //postSpiralImageFile.createNewFile();
                    FileOutputStream outputStream = new FileOutputStream(file);
                    spiralViewBitMap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    outputStream.close();
                    System.out.println("did work?");
                }
                catch (Throwable exception){
                    System.out.println("something went wrong :(");
                    exception.printStackTrace();
                }
                stopTime = System.currentTimeMillis();
                time = (stopTime - startTime);
                time = time /1000;
                SpiralActivity.textViewObj.setText(Double.toString(time) + " Seconds");
                Log.v("myTime",Double.toString(time) + "Seconds");
                break;
            default:
                return false;
        }

        //Completes the drawing
        invalidate();

        
        return true;
    }
}
