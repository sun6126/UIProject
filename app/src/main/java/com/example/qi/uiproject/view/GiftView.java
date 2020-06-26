package com.example.qi.uiproject.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.qi.uiproject.R;

// 刷礼物的布局效果
public class GiftView extends RelativeLayout {
    private int screenWidth;
    private int screenHeight;
    private RelativeLayout.LayoutParams layoutParams;
    private Drawable[] drawables = new Drawable[1];

    public GiftView(Context context) {
        super(context);
        init(context);
    }

    public GiftView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GiftView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        drawables[0] = ContextCompat.getDrawable(context, R.drawable.arrow);

        layoutParams = new LayoutParams(100, 100);
        //代码设置布局方式，底部居中
        layoutParams.addRule(CENTER_HORIZONTAL, TRUE);
        layoutParams.addRule(ALIGN_PARENT_BOTTOM, TRUE);
        
        // 设置本布局的点击事件
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addImageView();
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenWidth = w;
        screenHeight =h;
    }

    public void addImageView() {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageDrawable(drawables[(int) (Math.random() * drawables.length)]);
        imageView.setLayoutParams(layoutParams);

        addView(imageView);
        setAnim(imageView).start(); // 开启贝塞尔路线动画

        // 执行一个缩放动画
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(imageView, View.SCALE_X, 0.2f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(imageView, View.SCALE_Y, 0.2f, 1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(1000);
        animatorSet.playTogether(scaleX,scaleY);
        animatorSet.start();
    }

    private Animator setAnim(final ImageView imageView) {
        // 初始化一个贝塞尔计算器
        BezierEvaluator bezierEvaluator = new BezierEvaluator(getPointF(), getPointF());
        // 初始坐标在屏幕底部中心，结束坐标随机生成
        PointF endPointF = new PointF((float) Math.random() * screenWidth, (float) Math.random() * screenHeight);
        ValueAnimator valueAnimator = ValueAnimator.ofObject(bezierEvaluator, new PointF(screenWidth / 2, screenHeight), endPointF);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF pointF = (PointF) animation.getAnimatedValue();
                imageView.setX(pointF.x);
                imageView.setY(pointF.y);
                // 顺便做一个渐变动画
                float fraction = animation.getAnimatedFraction();
                imageView.animate().alpha(1-fraction);
            }
        });
        valueAnimator.setDuration(5000);
        return valueAnimator;
    }
    


    public static class BezierEvaluator implements TypeEvaluator<PointF> {

        private final PointF pointF1;
        private final PointF pointF2;

        public BezierEvaluator(PointF pointF1, PointF pointF2) {
            this.pointF1 = pointF1;
            this.pointF2 = pointF2;
        }

        @Override
        public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
            float timeLeft = 1.0f - fraction;
            PointF point = new PointF();//结果

            point.x = timeLeft * timeLeft * timeLeft * (startValue.x)
                    + 3 * timeLeft * timeLeft * fraction * (pointF1.x)
                    + 3 * timeLeft * fraction * fraction * (pointF2.x)
                    + fraction * fraction * fraction * (endValue.x);

            point.y = timeLeft * timeLeft * timeLeft * (startValue.y)
                    + 3 * timeLeft * timeLeft * fraction * (pointF1.y)
                    + 3 * timeLeft * fraction * fraction * (pointF2.y)
                    + fraction * fraction * fraction * (endValue.y);
            return point;
        }
    }

    // 产生随机控制点
    private PointF getPointF() {
        PointF pointF = new PointF();
        pointF.x = (float) (Math.random() * screenWidth);
        pointF.y = (float) (Math.random() * screenHeight / 4);
        return pointF;
    }
}
