package com.example.qi.uiproject.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

/*
 * 二阶贝塞尔曲线
 * */
public class QuadToView extends View {

    private int startX, startY;
    private int endX, endY;

    private int eventX, eventY; // 控制点
    private int centerX, centerY;
    private Paint mPaint;

    public QuadToView(Context context) {
        super(context);
        init();
    }

    public QuadToView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public QuadToView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }


    /*
    * 测量大小完成以后进行的回调
    * */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w/2; // 绘制中心在当前view的中心位置
        centerY = h/2;
        startX = centerX -250;
        startY = centerY;
        endX = centerX + 250;
        endY = centerY;
        eventX = centerX;
        eventY = centerY - 250;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.GRAY);

        // 画三个点
        canvas.drawCircle(startX,startY,8,mPaint);
        canvas.drawCircle(eventX,eventY,8,mPaint);
        canvas.drawCircle(endX,endY,8,mPaint);

        // 连接三个点
        mPaint.setStrokeWidth(3);
        canvas.drawLine(startX,startY,eventX,eventY,mPaint);
        canvas.drawLine(eventX,eventY,endX,endY,mPaint);

        // 画二阶贝塞尔曲线
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.STROKE);
        Path path = new Path();
        path.moveTo(startX,startY);
        path.quadTo(eventX,eventY,endX,endY);
        canvas.drawPath(path,mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE :
                eventX = (int) event.getX();
                eventY = (int) event.getY();
                invalidate();
                break;
        }
        return true;
    }
}














