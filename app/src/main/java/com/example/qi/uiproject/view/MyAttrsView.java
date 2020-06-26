package com.example.qi.uiproject.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.qi.uiproject.R;

public class MyAttrsView extends View {
    Paint mPaint;

    private int myAge;
    private String myName;
    private Drawable myBg;

    public MyAttrsView(Context context) {
        super(context);
    }

    public MyAttrsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(60);
        initView(context,attrs);
    }

    public MyAttrsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(String.valueOf(myAge),getMeasuredWidth()/3,60,mPaint);
        canvas.drawText(myName,getMeasuredWidth()/3,120,mPaint);

        BitmapDrawable bitmapDrawable = (BitmapDrawable) this.myBg;
        canvas.drawBitmap(bitmapDrawable.getBitmap(),getMeasuredWidth()/3,140,mPaint);
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyAttrs);
        myAge = typedArray.getInt(R.styleable.MyAttrs_my_age, 20);
        myName = typedArray.getString(R.styleable.MyAttrs_my_name);
        myBg = typedArray.getDrawable(R.styleable.MyAttrs_my_bg);
        typedArray.recycle();
    }


}
