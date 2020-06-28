package com.example.qi.uiproject.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

public class FlowLayout extends ViewGroup {

    boolean isFlag;

    // 定义保存每行最大高度的列表(为了得到每一行的最大高度，求和就是本layout的高度)
    List<Integer> lineHeightList = new ArrayList<>();

    // 定义保存每一行的view的列表的列表
    List<List<View>> lineViewList = new ArrayList<>();

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        lineHeightList.clear();
        lineViewList.clear();

//        // 增加一个flag使他不用绘制两次???
//        if (isFlag) {
//            return;
//        }
//        isFlag = true;

        // 获取父view传来的宽高值
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        // 获取父view传来的模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        // 测量真正的大小
        int measureWidth = 0;
        int measureHeight = 0;

        // 定义当前行的宽的实际使用大小，高的最大值
        int iCurrentLineW = 0;
        int iCurrentLineH = 0;

        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            measureWidth = widthSize;
            measureHeight = heightSize;
        } else { // 遍历子view来获取宽高

            // 定义当前子view的实际占用宽高
            int iChildWidth = 0;
            int iChildHeight = 0;

            // 定义存放每一行的view的list
            List<View> viewList = new ArrayList<>();

            for (int i = 0; i < getChildCount(); i++) {
                // 测量每个子view大小
                View child = getChildAt(i);
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
                // 获取该子view的布局参数,即xml资源中定义的参数
                MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
                // 得到子view实际占用的宽度
                iChildWidth = child.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
                iChildHeight = child.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;

                // 判断是否需要换行
                if (iCurrentLineW + iChildWidth > widthSize) { // 因为wrap_content模式下最大值就是widthSize，所以不能超过这个值
                    measureWidth = Math.max(measureWidth, iCurrentLineW); // 布局宽度 = 所有行里最宽的宽度
                    measureHeight += iCurrentLineH; // 布局高度 = 原来高度 + 新行的最大高度

                    // ？？？
                    lineHeightList.add(iCurrentLineH);
                    lineViewList.add(viewList); // 只添加了之前行里面的view

                    // 记录新行的宽高
                    iCurrentLineW = iChildWidth;
                    iCurrentLineH = iChildHeight;

                    viewList = new ArrayList<>();
                    viewList.add(child);
                } else { // 不用换行
                    // 当前行的实际使用宽度 = 实际使用的宽 + 新的view的宽
                    iCurrentLineW += iChildWidth;
                    // 求当前行的最大高度
                    iCurrentLineH = Math.max(iCurrentLineH, iChildHeight);

                    viewList.add(child);
                }

                //6.如果正好是最后一行需要换行
                if (i == getChildCount() - 1) { // 最后一行没有超过宽度，所以需要添加到list中以便布局使用
                    //6.1.记录当前行的最大宽度，高度累加
                    measureWidth = Math.max(measureWidth, iCurrentLineW);
                    measureHeight += iCurrentLineH;

                    //6.2.将当前行的viewList添加至总的mViewsList，将行高添加至总的行高List
                    lineViewList.add(viewList);
                    lineHeightList.add(iCurrentLineH);
                }
            }
        }
        //根据 布局里面设置的是wrap_content还是match_parent来决定当前FlowLayout的实际宽高
        measureWidth = widthMode == MeasureSpec.EXACTLY ? widthSize : measureWidth;
        measureHeight = heightMode == MeasureSpec.EXACTLY ? heightSize : measureHeight;
        setMeasuredDimension(measureWidth, measureHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int left, top, right, bottom;
        // 当前顶部高度和左部宽度
        int curTop = 0;
        int curLeft = 0;

        // 遍历viewlist
        for (int i = 0; i < lineViewList.size(); i++) {
            List<View> viewList = lineViewList.get(i);
            for (int j = 0; j < viewList.size(); j++) {
                View view = viewList.get(j);
                MarginLayoutParams layoutParams = (MarginLayoutParams) view.getLayoutParams();
                left = curLeft + layoutParams.leftMargin;
                top = curTop + layoutParams.topMargin;
                right = left + view.getMeasuredWidth();
                bottom = top + view.getMeasuredHeight();
                view.layout(left, top, right, bottom); // 计算出坐标然后布局

                curLeft = curLeft + view.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
            }
            // 准备换行前重置数据
            curLeft = 0;
            curTop += lineHeightList.get(i);
        }

        lineViewList.clear();
        lineHeightList.clear();

    }


    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
