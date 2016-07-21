package com.test.myapplication2.app;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by mina on 7/9/16.
 */
public class TimerView extends View  {

    String time;
    int angle;
    int width;
    int height;
    int r;

    public TimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        time = "Start!";
        angle = 0;
        setFocusable(true);
        setClickable(true);
    }


    public void setAngle(int angle) {
        this.angle = angle;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = widthSize;
        int desiredHeight = width;
        int height;

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else {
            //Be whatever you want
            height = desiredHeight;
        }

        //MUST CALL THIS
        this.height = height;
        this.width = width;

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        Log.d("TAG", "onDraw ");

//        super.onDraw(canvas);
        Paint p = new Paint();
        p.setStyle(Paint.Style.STROKE);
        p.setColor(getResources().getColor(R.color.primary_light));
        final RectF oval = new RectF();

        r = 2 * width / 5;
        int leftx = width / 2 - r;
        int rightx = width / 2 + r;
        int topy = height / 2 - r;
        int bottomy = height / 2 + r;

        oval.set(leftx, topy, rightx, bottomy);
//        angle = 90;
        p.setStrokeWidth(width/70);
        canvas.drawArc(oval, 270, angle, false, p);
        p.setColor(getResources().getColor(R.color.primary_dark));
        canvas.drawArc(oval, 270 + angle, 360 - angle, false, p);

        Paint textPaint = new Paint();
//        textPaint.setColor(getResources().getColor(R.color.secondary_text_material_light));
        textPaint.setTextAlign(Paint.Align.CENTER);
        float size = r / 3;
        textPaint.setTextSize(size);
        canvas.drawText(time, width / 2, height / 2 + size / 3 , textPaint);
//        canvas.drawText("salam",50 , 50 , p);
//        Log.d("TAG", "onDraw end ");

    }

    public int getRadious(){
        return r;
    }

    public int getCenterX(){
        return width/2;
    }

    public int getCenterY(){
        return height/2;
    }
}