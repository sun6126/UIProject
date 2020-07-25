package com.example.qi.uiproject.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

public class ZeeFlowLayout extends ViewGroup {
    private static final String TAG = "FlowLayout";
    //定义一个大容器，用于存放每一行的子控件
    private List<List<View>> totalList_View = new ArrayList<>();
    //定义一个容器，用于存放每一行最大的高度
    private List<Integer> totalList_Height = new ArrayList<>();
    //是否垂直居中
    private boolean isCenterVertical;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ZeeFlowLayout(Context context) {
        this(context, null, 0, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ZeeFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ZeeFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ZeeFlowLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        //获取自定义属性
//        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
//        isCenterVertical = typedArray.getBoolean(R.styleable.FlowLayout_center_vertical, false);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    /**
     * 1、测量当前控件的大小
     * 2、在测量过程当中，计算出每一行的最大的高度
     * 3、还要把所有的子控件搜集起来
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d("FlowLayout", "onMeasure");
        totalList_Height.clear();
        totalList_View.clear();
        //获取当前控件的测量模式和测量大小
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        //定义实际的宽高
        int realWidth = 0;
        int realHeight = 0;

        //定义一个累加变量，用来保存当前行已经使用的宽度
        int currentLineUsedWidth = 0;
        //当前行最高的高度
        int currentLineMaxHeight = 0;
        //定义一个容器用于存放每一行的子控件
        ArrayList<View> viewList = new ArrayList<>();
        //遍历所有的子控件
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            //测量他的子控件以决定当前控件的宽高（前提是wrap_content）
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
            //此时意味着所有的子控件全部测量完毕
//            得到子控件的宽高
            int childMeasuredWidth =
                    child.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
            int childMeasuredHeight =
                    child.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
            //解决流式布局的核心问题：换行
            if (childMeasuredWidth + currentLineUsedWidth > widthSize) {
                //换行
                //把上一行所有的子控件丢到大容器当中
                totalList_View.add(viewList);
                //保存上一行最高的高度
                totalList_Height.add(currentLineMaxHeight);
                //取所有行里面最宽的宽度作为FlowLayout的实际的宽
                realWidth = Math.max(realWidth, currentLineUsedWidth);
                //所有行的最高高度累加作为FlowLayout的实际的高
                realHeight += currentLineMaxHeight;
                //当前行值重置
                currentLineUsedWidth = childMeasuredWidth;
                currentLineMaxHeight = childMeasuredHeight;
                //给当前行重新创建一个view容器，并把当前子控件扔进去
                viewList = new ArrayList<>();
                viewList.add(child);
            } else {
                //不换行
                //找出当前行最大的高度
                currentLineMaxHeight = Math.max(childMeasuredHeight, currentLineMaxHeight);
                //把当前子控件丢到当前行的容器当中
                viewList.add(child);
                //对当前行已经使用的宽高累加
                currentLineUsedWidth += childMeasuredWidth;
            }
            //最后一行特殊处理
            if (i == childCount - 1) {
                totalList_View.add(viewList);
                //保存上一行最高的高度
                totalList_Height.add(currentLineMaxHeight);
                //取所有行里面最宽的宽度作为FlowLayout的实际的宽
                realWidth = Math.max(realWidth, currentLineUsedWidth);
                //所有行的最高高度累加作为FlowLayout的实际的高
                realHeight += currentLineMaxHeight;
            }
        }

        //根据 布局里面设置的是wrap_content还是match_parent来决定当前FlowLayout的实际宽高
        realWidth = widthMode == MeasureSpec.EXACTLY ? widthSize : realWidth;
        realHeight = heightMode == MeasureSpec.EXACTLY ? heightSize : realHeight;

        setMeasuredDimension(realWidth, realHeight);
    }

    /**
     * 开始布局，布局就是找到所有子控件在FlowLayout当中的坐标
     *
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d("FlowLayout", "onLayout");
        int left, top, right, bottom;
        //定义一个x坐标的累加变量
        int currentX = 0;
        //定义一个y坐标的累加变量
        int currentY = 0;
        //遍历所有的子控件，找到他的四个坐标
        int count = 0;
        int size = totalList_View.size();
        for (int i = 0; i < size; i++) {
            List<View> viewList = totalList_View.get(i);
            int size1 = viewList.size();
            for (int j = 0; j < size1; j++) {
                count++;
                View child = viewList.get(j);
                int childMeasuredWidth = child.getMeasuredWidth();
                int childMeasuredHeight = child.getMeasuredHeight();
                MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
                //计算四个坐标
                left = currentX + layoutParams.leftMargin;
                if (isCenterVertical) {
                    top = currentY + (totalList_Height.get(i) - childMeasuredHeight) / 2;
                } else {
                    top = currentY + layoutParams.topMargin;
                }
                right = left + childMeasuredWidth;
                bottom = top + childMeasuredHeight;
                child.layout(left, top, right, bottom);

                //当前行横坐标累加
                currentX += childMeasuredWidth + layoutParams.leftMargin + layoutParams.rightMargin;
            }
            //y坐标累加
            currentY += totalList_Height.get(i);
            //当前横坐标重置
            currentX = 0;
        }
        Log.d(TAG, "count:" + count);
        totalList_View.clear();
        totalList_Height.clear();
    }
}