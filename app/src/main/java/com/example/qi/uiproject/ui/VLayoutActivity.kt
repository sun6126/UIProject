package com.example.qi.uiproject.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AbsListView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.alibaba.android.vlayout.layout.StickyLayoutHelper
import com.example.qi.uiproject.R
import com.example.qi.uiproject.adapter.VLayoutAdapter
import kotlinx.android.synthetic.main.activity_v_layout.*
import java.nio.charset.MalformedInputException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class VLayoutActivity : AppCompatActivity() {

    // 数据源
    var dataList = ArrayList<HashMap<String, Any>>()

    // 适配器
    var linearLayoutAdapter: VLayoutAdapter? = null;
    var stickLayoutAdapter: VLayoutAdapter? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_v_layout)
        initData()
        // 原先的做法
//        recyclerView.layoutManager = LinearLayoutManager(VLayoutActivity@ this)

        // 使用VLayout(VLayout对LayoutManager的封装)
        val virtualLayoutManager = VirtualLayoutManager(this)
        recyclerView.layoutManager = virtualLayoutManager
        // 给recyclerView设置回收池
        val recycledViewPool = recyclerView.recycledViewPool
        // 设置recyclerview的item类型，以及最大数量
        recycledViewPool.setMaxRecycledViews(0, 10)

        // 定义一个集合存储要使用的adaper（也可以不用集合）
        val adapterList  = LinkedList<DelegateAdapter.Adapter<RecyclerView.ViewHolder>>()
        // 定义一个总的adapter类
        val delegateAdapter = DelegateAdapter(virtualLayoutManager)



        // 线性布局 -------------------------------------------》
        val linearLayoutHelper = LinearLayoutHelper()
        // 设置数据总行数(作用不大，一般在adapter中设置)
        linearLayoutHelper.itemCount = 4
        // 设置itemview的padding和margin值
        linearLayoutHelper.setPadding(10, 10, 10, 10)
//        linearLayoutHelper.setMargin(2, 2, 2, 2)
        // 设置宽高比
//        linearLayoutHelper.aspectRatio = 3f
        // 设置分割线高度
        linearLayoutHelper.setDividerHeight(6)
        linearLayoutAdapter = VLayoutAdapter(dataList, this, 20, linearLayoutHelper);

        adapterList.add(linearLayoutAdapter as DelegateAdapter.Adapter<RecyclerView.ViewHolder>)
        delegateAdapter.addAdapters(adapterList)

        // 定格布局 --------------------------------------------》
        val stickyLayoutHelper = StickyLayoutHelper()
        stickyLayoutHelper.setStickyStart(false)
        stickyLayoutHelper.setPadding(10, 10, 10, 10)
        stickLayoutAdapter = VLayoutAdapter(dataList,this,1,stickyLayoutHelper)
        delegateAdapter.addAdapter(stickLayoutAdapter)


        recyclerView.adapter = delegateAdapter


    }

    private fun initData() {
        dataList = ArrayList<HashMap<String, Any>>()
        for (i in 0..100) {
            val hashMap = hashMapOf<String, Any>()
            hashMap["image"] = R.mipmap.ic_launcher
            hashMap["text"] = "第 $i 个数据"
            dataList.add(hashMap)
        }
    }
}
