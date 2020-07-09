package com.example.qi.uiproject.material;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;

import com.example.qi.uiproject.view.NestedLinearLayout;

public class NestedBehavior {
    public NestedBehavior(Context context) {
    }

    /*
     * 筛选被观察者的方法
     * */
    public boolean layoutDependentsOn(NestedLinearLayout parent, View child, View dependency) {
        return dependency instanceof NestedScrollView;
    }

    /*
     * 观察者要做的事情
     * 正在滑动所做的事
     * childAt:观察者
     * */
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, View childAt, NestedLinearLayout parent) {

        // 根据dyConsumed可以确定是往上滑动还是往下滑动（上正下负）
        if (dyConsumed < 0) { // 向下滑动
            // 观察者的y坐标不能小于0，观察者的滑动距离不能多于他的高度
            if (childAt.getY() <= 0 && target.getY() < childAt.getHeight()) {
                childAt.setTranslationY(-(target.getScrollY() > childAt.getHeight() ? childAt.getHeight() : target.getScrollY()));

                target.setTranslationY(-(target.getScrollY() > childAt.getHeight() ?
                        childAt.getHeight() : target.getScrollY()));
                ViewGroup.LayoutParams layoutParams = target.getLayoutParams();
                layoutParams.height = (int) (parent.getHeight() - childAt.getHeight() - childAt.getTranslationY());
                target.setLayoutParams(layoutParams);
            }
        } else { // 向上滑动
            //向上滑动了 被观察者的Y坐标不能小于或者等于0
            if (target.getY() > 0) {
                //设置观察者的Y坐标的偏移  1.不能超过观察者自己的高度
                childAt.setTranslationY(-(target.getScrollY() > childAt.getHeight() ? childAt.getHeight() : target.getScrollY()));

                target.setTranslationY(-(target.getScrollY() > childAt.getHeight() ?
                        childAt.getHeight() : target.getScrollY()));
                //获取到被观察者的LayoutParams
                ViewGroup.LayoutParams layoutParams = target.getLayoutParams();
                //当我们向上滑动的时候  被观察者的高度 就等于 它父亲的高度 减去观察者的高度 再减去观察者Y轴的偏移值
                layoutParams.height = (int) (parent.getHeight() - childAt.getHeight() - childAt.getTranslationY());
                target.setLayoutParams(layoutParams);
            }
        }
    }


}
