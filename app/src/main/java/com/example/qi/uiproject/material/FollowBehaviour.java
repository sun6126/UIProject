package com.example.qi.uiproject.material;

import android.content.Context;
import android.media.MicrophoneInfo;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

public class FollowBehaviour extends CoordinatorLayout.Behavior<TextView> {

    boolean isOne = true;

    // 必须要写此构造，会被反射创建对象
    public FollowBehaviour(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // 拦截布局，查找到需要依赖的控件
    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull TextView child, @NonNull View dependency) {
        super.layoutDependsOn(parent, child, dependency);
        if (dependency instanceof Button){ // 观察的类型为Button
            return true;
        }
        return false;
    }

    // 根据依赖的控件来指定自己的行为
    // 只有在被依赖的控件 大小、位置发生改变时才被调用
    @Override
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull TextView child, @NonNull View dependency) {
        super.onDependentViewChanged(parent, child, dependency);
        if (isOne){
            isOne = false;
            return true;
        }
        child.setX(dependency.getX() + 200);
        child.setY(dependency.getY() + 200);
        return true;
    }


    /*
    * 当coordinatorLayout的子视图试图开始嵌套滚动的时候回调
    * 这个方法只有返回true时，behavior才会接受到后面一些 nested scroll的回调
    * axes表示处理事件的滑动方向
    * */
    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull TextView child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type);
    }


}
