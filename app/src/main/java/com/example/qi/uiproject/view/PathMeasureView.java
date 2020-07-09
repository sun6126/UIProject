package com.example.qi.uiproject.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.qi.uiproject.R;


/*
* 根据path测量位置角度的自定义view
* */
public class PathMeasureView extends View {

    private float currentValue = 0; // 用于记录当前长度，取值范围【0，1】映射path的整个长度
    private float[] pos; // 当前点的实际位置
    private float[] tan; // 当前点的tan值，用于计算图片所需旋转的角度
    private Bitmap mBitmap; // 箭头图片
    private Matrix mMatrix; // 用于对图片进行进一步操作的矩阵
    private Paint mDefaultPaint; //
    private int mViewWidth;
    private int mViewHeight;

    private Paint mPaint;


    public PathMeasureView(Context context) {
        super(context);
        init(context);
    }

    public PathMeasureView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PathMeasureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PathMeasureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
    }

    
    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 平移坐标系
        canvas.translate(mViewWidth / 2, mViewHeight / 2);
        // 画坐标系
        canvas.drawLine(-getWidth(), 0, getWidth(), 0, mPaint);
        canvas.drawLine(0, -getHeight(), 0, getHeight(), mPaint);

        // 画圆
        Path path = new Path();
        path.addCircle(0, 0, 200, Path.Direction.CW);

        // 创建一个pathMeasure与圆环关联
        PathMeasure pathMeasure = new PathMeasure(path, false);

        currentValue += 0.005; // 系数
        if (currentValue >= 1) {
            currentValue = 0;
        }

        /*
        * 方案一：获取矩阵，包含角度移动距离
        * 获取path上指定长度的矩阵？？？ 获取的矩阵传入mMatrix中
        * pathMeasure.getLength得到圆环的总长，再乘以系数来进行移动
        * */
        pathMeasure.getMatrix(pathMeasure.getLength() * currentValue, mMatrix, PathMeasure.POSITION_MATRIX_FLAG | PathMeasure.TANGENT_MATRIX_FLAG);
        // 将图标偏移到圆环上
        mMatrix.preTranslate(-mBitmap.getWidth() / 2, -mBitmap.getHeight() / 2);

        /*
        * 方案二：获取当前位置的坐标以及趋势
        *
        * */
        pathMeasure.getPosTan(pathMeasure.getLength() * currentValue,pos,tan);
        Log.e("posTan", "postion" + pos[0] + "  "+pos[1]);
        Log.e("posTan", tan[0]+ "   "+tan[1]);
//        canvas.drawCircle(tan[0],tan[1],20,mDefaultPaint);
//          // 重置matrix
//        mMatrix.reset();
//          // 计算图片旋转角度（或弧度）
//        float degree = (float) Math.atan2(tan[1],tan[0]*180.0/Math.PI);
            // 旋转图片
//        mMatrix.postRotate(degree,mBitmap.getWidth()/2,mBitmap.getHeight()/2);
//          // 将图片绘制中心调整到与当前点重合
//        mMatrix.postTranslate(pos[0]- mBitmap.getWidth()/2,pos[1]-mBitmap.getHeight()/2)

        canvas.drawPath(path, mDefaultPaint);
        // 画三角形（bitmap）
        canvas.drawBitmap(mBitmap, mMatrix, mDefaultPaint);
        invalidate();
    }

    private void init(Context context) {
        pos = new float[2];
        tan = new float[2]; // pathMeasure.getPosTan的时候会用到
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.arrow, options);

        mMatrix = new Matrix();

        mDefaultPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDefaultPaint.setColor(Color.RED);
        mDefaultPaint.setStrokeWidth(5);
        mDefaultPaint.setStyle(Paint.Style.STROKE);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.DKGRAY);
        mPaint.setStrokeWidth(2);
        mPaint.setStyle(Paint.Style.STROKE);
    }


}
