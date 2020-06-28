package com.example.qi.uiproject.layoutManager;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qi.uiproject.listener.OnViewPagerListener;

public class DouYinLayoutManager extends LinearLayoutManager implements RecyclerView.OnChildAttachStateChangeListener {

    // 根据此参数判断向上滑还是向下滑
    private int mDrift;

    private OnViewPagerListener viewPagerListener;

    //解决吸顶或者洗低的对象
    private PagerSnapHelper pagerSnapHelper;
    private int flag = -1;

    public DouYinLayoutManager(Context context) {
        super(context);
    }

    public DouYinLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        pagerSnapHelper = new PagerSnapHelper();

    }

    // 当layoutmanager初始化完毕并加入到recyclerview中后会调用
    @Override
    public void onAttachedToWindow(RecyclerView view) {
        view.addOnChildAttachStateChangeListener(this); // 监听itemView的状态
        pagerSnapHelper.attachToRecyclerView(view); // 解决吸顶和吸底
        super.onAttachedToWindow(view);

    }

    /*
     * dy表示滑动距离
     * */
    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        mDrift = dy;
        return super.scrollVerticallyBy(dy, recycler, state);
    }

    /*
     * 可以垂直滑动
     * */
    @Override
    public boolean canScrollVertically() {
        return super.canScrollVertically();
    }


    /*
     * 当itemview添加到窗口中时调用
     *
     *  这边控制播放会使得上下两个页都在播放，可以使用监听滑动状态来播放 onScrollStateChanged(int state)
     * 但是第一个view需要在这里控制
     * */
    @Override
    public void onChildViewAttachedToWindow(@NonNull View view) {
        if (flag < 0){
            viewPagerListener.onPageSelected(view);
        }
//        if (mDrift > 0) { // 向上滑时，应该播放当前添加进来的itemview
//            if (viewPagerListener != null) {
//                viewPagerListener.onPageSelected(view);
//            }
//        } else { // 向下滑动时
//            if (viewPagerListener != null) {
//                viewPagerListener.onPageSelected(view);
//            }
//        }
    }

    /*
     * 当itemview被移除时调用
     * */
    @Override
    public void onChildViewDetachedFromWindow(@NonNull View view) {
        if (mDrift > 0) { // 向上滑时，应该关闭移除的itemview的视频
            if (viewPagerListener != null) {
                viewPagerListener.onPageRelease(view);
            }
        } else { // 向下滑动时
            if (viewPagerListener != null) {
                viewPagerListener.onPageRelease(view);
            }
        }
    }

    /*
     * 监听滑动的状态
     * */
    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        switch (state) {
            case RecyclerView.SCROLL_STATE_IDLE: // 滑动停止状态
                // 当前显示的item(只有整屏的item可以这么来)
                flag = 1;
                View snapView = pagerSnapHelper.findSnapView(this);
                viewPagerListener.onPageSelected(snapView);
                break;
            case RecyclerView.SCROLL_STATE_SETTLING: // 惯性滑动
                break;
        }
    }

    public void setViewPagerListener(OnViewPagerListener viewPagerListener) {
        this.viewPagerListener = viewPagerListener;
    }
}
