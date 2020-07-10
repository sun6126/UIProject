package com.example.qi.uiproject.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class WatchView extends View {

    int width;
    int height;


    public WatchView(Context context) {
        super(context);
    }

    public WatchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WatchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public WatchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 1、画外圆
        Paint paintCiecle = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintCiecle.setStrokeWidth(5);
        paintCiecle.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(width / 2, height / 2, width / 2, paintCiecle);

        // 2、画刻度线（刻度线长短判断）
        Paint paintDegree = new Paint(Paint.ANTI_ALIAS_FLAG);
        // 画12根刻度线，12、3、6、9的刻度线长点
        for (int i = 0; i < 12; i++) {
            if (i == 0 || i == 3 || i == 6 || i == 9) {
                paintDegree.setStrokeWidth(5);
                paintDegree.setTextSize(100);
                canvas.drawLine(width / 2, height / 2 - width / 2,
                        width / 2, height / 2 - width / 2 + 60,
                        paintDegree);
                String degree = String.valueOf(i);
                canvas.drawText(degree, width / 2 - paintDegree.measureText(degree) / 2,
                        height / 2 - width / 2 + 160, paintDegree);

            } else {

                paintDegree.setStrokeWidth(3);
                paintDegree.setTextSize(50);
                canvas.drawLine(width / 2, height / 2 - width / 2,
                        width / 2, height / 2 - width / 2 + 30,
                        paintDegree);
                String degree = String.valueOf(i);
                canvas.drawText(degree, width / 2 - paintDegree.measureText(degree) / 2,
                        height / 2 - width / 2 + 60, paintDegree);
            }

            // 每次画完一个坐标系旋转三十度(其实就是改变下笔的位置)
            canvas.rotate(30, width / 2, height / 2);
        }

        // 画指针
        Paint paintHour = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintHour.setStrokeWidth(20);
        Paint paintMinute = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintMinute.setStrokeWidth(10);
        // 改变下笔位置到圆心
        canvas.translate(width/2,height/2);
        canvas.drawLine(0,0,60,60,paintHour);
        canvas.drawLine(0,0,40,120,paintMinute);


    }
}
