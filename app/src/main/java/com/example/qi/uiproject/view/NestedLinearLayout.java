package com.example.qi.uiproject.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.view.NestedScrollingParent2;

import com.example.qi.uiproject.R;
import com.example.qi.uiproject.material.NestedBehavior;

import java.lang.reflect.Constructor;

public class NestedLinearLayout extends LinearLayout implements NestedScrollingParent2 {
    public NestedLinearLayout(Context context) {
        super(context);
    }

    public NestedLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NestedLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public NestedLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    /*
     * 这里返回true，后续的滚动方法才会执行
     * */
    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        return true;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {

    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {

    }


    /*
     *  接收滚动的通知，子nested系列view一直滚动，这个方法就一直调用
     * type : 发起嵌套事件的类型，分为触摸和非触摸（viewParent.TYPE_TOUCH）和非触摸（viewParent.TYPE_NON_TOUCH）
     * */
    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        // 遍历所有的子控件，看他们有没有设置behavior
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            // 从layoutparams里面获取behavior
            MyLayoutParams layoutParams = (MyLayoutParams) childAt.getLayoutParams();
            NestedBehavior behavior = layoutParams.behavior;
            if (behavior != null) {
                if (behavior.layoutDependentsOn(this, childAt, target)) {
                    behavior.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type,childAt,this);
                }
            }
        }

    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {

    }


    /*
     * 生成所有子控件所需要的layoutparams 属性
     * */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MyLayoutParams(getContext(), attrs);
    }

    static class MyLayoutParams extends LayoutParams {

        public NestedBehavior behavior = null;

        public MyLayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            // 将自定义属性交给一个typeArray来管理
            TypedArray typedArray = c.obtainStyledAttributes(attrs,R.styleable.NestedLinearLayout);
            // 再获取到我们在xml中给自定义属性的值（这边是得到全类名） 可能为空
            String className = typedArray.getString(R.styleable.NestedLinearLayout_layout_behavior);
            // 根据类名将Behavior实例化
            behavior = parseBehavior(c, attrs, className);
            typedArray.recycle();

        }

        private NestedBehavior parseBehavior(Context c, AttributeSet attrs, String className) {
            NestedBehavior behavior = null;
            if (TextUtils.isEmpty(className)) {
                return null;
            } else {
                try {
                    Class aClass = Class.forName(className);
                    // 获取他的构造方法
                    Constructor<? extends NestedBehavior> constructor = aClass.getConstructor(Context.class);
                    constructor.setAccessible(true);
                    behavior = constructor.newInstance(c);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return behavior;
            }
        }

        public MyLayoutParams(int width, int height) {
            super(width, height);
        }

        public MyLayoutParams(int width, int height, float weight) {
            super(width, height, weight);
        }

        public MyLayoutParams(ViewGroup.LayoutParams p) {
            super(p);
        }

        public MyLayoutParams(MarginLayoutParams source) {
            super(source);
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public MyLayoutParams(LayoutParams source) {
            super(source);
        }
    }

}
