package com.example.qi.uiproject.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.BounceInterpolator;

import androidx.annotation.Nullable;

import com.example.qi.uiproject.animateUtil.PointEvaluator;
import com.example.qi.uiproject.bean.Point;

/*
* 圆球移动的动画
* 自定义evaluator
* */
public class MyAnimView extends View {

    public static final float RADIUS = 50f;
    private Point currentPoint;
    private Paint mPaint;
    private String color; // 画笔颜色

    public String getColor(){
        return color;
    }

    public void setColor(String value){
        color = value;
        mPaint.setColor(Color.parseColor(color));
        invalidate();
    }


    public MyAnimView(Context context) {
        super(context);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLUE);
    }

    public MyAnimView(Context context, @Nullable AttributeSet attrs) { // 在xml中使用会调用这个构造方法
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLUE);
    }

//    public MyAnimView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mPaint.setColor(Color.BLUE);
//    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (currentPoint == null){
            currentPoint = new Point(RADIUS,RADIUS);
            drawCircle(canvas);
            startAnimation();
        }else {
            drawCircle(canvas);
        }
    }

    private void startAnimation() {
//        Point startPoint = new Point(RADIUS, RADIUS);
//        Point endPoint = new Point(getWidth() - RADIUS, getHeight() - RADIUS);
        // 改成垂直降落
        Point startPoint = new Point(getWidth()/2, RADIUS);
        Point endPoint = new Point(getWidth()/2, getHeight() - RADIUS);

        PointEvaluator pointEvaluator = new PointEvaluator();
        ValueAnimator animator = ValueAnimator.ofObject(pointEvaluator, startPoint, endPoint);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentPoint = (Point) animation.getAnimatedValue();
                invalidate();
                post(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        });
        // 可以给动画增interpolator来改变动画的运行的线性效果
//        animator.setInterpolator(new AccelerateInterpolator(2f)); // 加速效果，2f是控制加速度的
        animator.setInterpolator(new BounceInterpolator()); // 回弹效果
        animator.setDuration(3000);
        animator.start();
    }

    private void drawCircle(Canvas canvas) {
        float x = currentPoint.getX();
        float y = currentPoint.getY();
        canvas.drawCircle(x,y,RADIUS,mPaint);
    }
}
