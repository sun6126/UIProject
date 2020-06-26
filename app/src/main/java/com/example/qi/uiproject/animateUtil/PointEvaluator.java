package com.example.qi.uiproject.animateUtil;

import android.animation.TypeEvaluator;

import com.example.qi.uiproject.bean.Point;

public class PointEvaluator implements TypeEvaluator { // 作用就是告诉动画系统如何从初始值过度到结束值。
    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {
//        第一个参数fraction非常重要，这个参数用于表示动画的完成度的，
//        我们应该根据它来计算当前动画的值应该是多少，第二第三个参数分别表示动画的初始值和结束值。
        Point startPoint = (Point) startValue;
        Point endPoint = (Point) endValue;
        float x = startPoint.getX() + fraction * (endPoint.getX() - startPoint.getX());
        float y = startPoint.getY() + fraction * (endPoint.getY() - startPoint.getY());
        Point point = new Point(x, y);
        return point;
    }
}
