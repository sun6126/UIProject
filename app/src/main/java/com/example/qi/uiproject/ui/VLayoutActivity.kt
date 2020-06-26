package com.example.qi.uiproject.ui

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AbsListView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.layout.*
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
    var gridLayoutAdapter: VLayoutAdapter? = null;

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
        val adapterList = LinkedList<DelegateAdapter.Adapter<RecyclerView.ViewHolder>>()
        // 定义一个总的adapter类
        val delegateAdapter = DelegateAdapter(virtualLayoutManager)


        // 1、线性布局 -------------------------------------------》
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

        // 2、定格布局 --------------------------------------------》
        val stickyLayoutHelper = StickyLayoutHelper()
        stickyLayoutHelper.setStickyStart(false)
        stickyLayoutHelper.setPadding(10, 10, 10, 10)
        stickLayoutAdapter = VLayoutAdapter(dataList, this, 1, stickyLayoutHelper)
        delegateAdapter.addAdapter(stickLayoutAdapter)

        // 3、栅格布局（一行里按比例排放的布局） ---------------------->
        val columnLayoutHelper = ColumnLayoutHelper()
        // 设置每个item占该行总宽度的比例
        columnLayoutHelper.setWeights(floatArrayOf(10f,10f,20f,30f,30f))
        val columnLayoutAdapter = VLayoutAdapter(dataList, this, 5, columnLayoutHelper)
        delegateAdapter.addAdapter(columnLayoutAdapter)

        // 4、网格布局 --------------------------------------------》
        // 指定列数
        val gridLayoutHelper = GridLayoutHelper(3)
//        gridLayoutHelper.aspectRatio = 0.6f
        // 设置权重，即一行中多个控件的比例（加起来要等于100）
        gridLayoutHelper.setWeights(floatArrayOf(30f, 30f, 30f))
        // 设置垂直，水平边距
        gridLayoutHelper.vGap = 10
        gridLayoutHelper.hGap = 4
        // 设置是否自动填充空白区域
        gridLayoutHelper.setAutoExpand(true)
        gridLayoutAdapter = VLayoutAdapter(dataList, this, 10, gridLayoutHelper)
        delegateAdapter.addAdapter(gridLayoutAdapter)

        // 5、悬浮布局 即 固定布局---------------------------------------------->
        val scrollFixLayoutHelper = ScrollFixLayoutHelper(ScrollFixLayoutHelper.BOTTOM_RIGHT,10,10)
        scrollFixLayoutHelper.showType = ScrollFixLayoutHelper.SHOW_ALWAYS
        val scrollFixAdapter = VLayoutAdapter(dataList, this, 1, scrollFixLayoutHelper)
        delegateAdapter.addAdapter(scrollFixAdapter)




        // 6、一加多布局（规定的左边一个，右边先上面一个，然后下面） ---->
        val onePlusNLayoutHelper = OnePlusNLayoutHelper()
        // 设置行比重
        onePlusNLayoutHelper.setColWeights(floatArrayOf(40f,60f,20f,30f))
        // 设置高比重(就是右边布局的高按比例分)
        onePlusNLayoutHelper.setRowWeight(50f)
        // 一加多布局最多只可以放5个子布局，记得要设置行比重
        val onePlusAdapter = VLayoutAdapter(dataList, this, 4, onePlusNLayoutHelper)
        delegateAdapter.addAdapter(onePlusAdapter)

        // 7、瀑布流布局
        val staggeredGridLayoutHelper = StaggeredGridLayoutHelper()
        // 共有属性
        staggeredGridLayoutHelper.itemCount = 20
        staggeredGridLayoutHelper.setMargin(10,10,10,10)
        staggeredGridLayoutHelper.setPadding(10,10,10,10)
        staggeredGridLayoutHelper.bgColor = Color.BLUE
        staggeredGridLayoutHelper.aspectRatio = 3f
        // 控制瀑布流每行item数
        staggeredGridLayoutHelper.lane = 2
        // 水平/垂直间距
        staggeredGridLayoutHelper.hGap = 6
        staggeredGridLayoutHelper.hGap = 10
        val staggeredGridLayoutAdapter = VLayoutAdapter(dataList, this, 60, staggeredGridLayoutHelper)
        delegateAdapter.addAdapter(staggeredGridLayoutAdapter)

        // 8、一个元素布局
        val singleLayoutHelper = SingleLayoutHelper()
        singleLayoutHelper.bgColor = Color.YELLOW
        val singleLayoutAdapter = VLayoutAdapter(dataList, this, 60, singleLayoutHelper)
        delegateAdapter.addAdapter(singleLayoutAdapter)



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
