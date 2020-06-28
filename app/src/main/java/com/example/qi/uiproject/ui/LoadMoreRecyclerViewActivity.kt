package com.example.qi.uiproject.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qi.uiproject.R
import com.example.qi.uiproject.adapter.SelfDefinedLoadMoreAdapter
import kotlinx.android.synthetic.main.activity_load_more_recycler_view.*
import java.util.*

class LoadMoreRecyclerViewActivity : AppCompatActivity() {

    val dataList = mutableListOf<String>()
    var selfDefinedLoadMoreAdapter: SelfDefinedLoadMoreAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_more_recycler_view)
        initData()
        initView()
        initListener()
    }

    private fun initListener() {
        loadMoreRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            //用来标记是否正在向上滑动
            private var isSlidingUpward = false

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                if (newState == RecyclerView.SCROLL_STATE_IDLE){ // 停止滑动时，改变adapter状态
                    //获取最后一个完全显示的itemPosition
                    val lastItemPosition: Int = layoutManager.findLastCompletelyVisibleItemPosition()
                    val itemCount: Int = layoutManager.getItemCount()

                    // 判断是否滑动到了最后一个item，并且是向上滑动
                    if (lastItemPosition == itemCount - 1 && isSlidingUpward) {
                        //加载更多
                        addData()
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                // 大于0表示正在向上滑动，小于等于0表示停止或向下滑动
                isSlidingUpward = dy > 0;
            }
        })
    }

    private fun initView() {
        loadMoreRecyclerView.layoutManager = LinearLayoutManager(this)
        selfDefinedLoadMoreAdapter = SelfDefinedLoadMoreAdapter(dataList)
        loadMoreRecyclerView.adapter = selfDefinedLoadMoreAdapter
    }

    private fun initData() {
        for (i in 1..20) {
            dataList.add("第$i 个数据")
        }
    }

    private fun addData() {
        if (dataList.size > 100 ){
            selfDefinedLoadMoreAdapter?.setLoadState(3)
            return
        }
        selfDefinedLoadMoreAdapter?.setLoadState(1)
        // 模拟获取网络数据，延时1s
        Timer().schedule(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    for (i in 1..10) {
                        dataList.add("新增第${dataList.size + i} 个数据")
                    }
                    selfDefinedLoadMoreAdapter?.setLoadState(2)
                }
            }
        }, 1000)
    }
}
