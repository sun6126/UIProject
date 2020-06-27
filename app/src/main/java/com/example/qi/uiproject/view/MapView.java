package com.example.qi.uiproject.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.PathParser;

import com.example.qi.uiproject.R;
import com.example.qi.uiproject.bean.ProvinceItem;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MapView extends View {
    private Context context;
    private Paint paint;
    // 所有省份的集合
    private List<ProvinceItem> itemList;

    // 被选中的省份
    ProvinceItem selectedProvinceItem;

    //绘制地图的颜色
    private int[] colorArray = new int[]{0xFF239BD7, 0xFF30A9E5, 0xFF80CBF1, 0xFFFFFFFF};
    // 地图的边界矩形
    RectF mapRectf = null;
    // 地图缩放比
    float scale;

    public MapView(Context context) {
        super(context);
        init(context);
    }

    public MapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        loadThread.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mapRectf != null) {
            // 获取宽度比例值 作为map的缩放比
            scale = getMeasuredWidth() / mapRectf.width();
        }
        if (itemList != null && itemList.size() > 0) {
            canvas.save();
            canvas.scale(scale, scale);
            for (int i = 0; i < itemList.size(); i++) {
                ProvinceItem provinceItem = itemList.get(i);
                // 一开始都是未被选中的
                provinceItem.drawItem(canvas, paint, false);
            }
            if (selectedProvinceItem != null) {
                selectedProvinceItem.drawItem(canvas, paint, true);
            }
            canvas.restore();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        handleTouchEvent(event.getX()/scale, event.getY()/scale);
        return true;
    }

    // 判断区域是否在某个省份
    private void handleTouchEvent(float x, float y) {
        if (itemList != null && itemList.size() > 0) {
            for (ProvinceItem provinceItem : itemList) {
                if (provinceItem.isTouch(x, y)) {
                    selectedProvinceItem = provinceItem;
                    invalidate();
                    break;
                }
            }
        }
    }

    /*
     * 创建线程，用来解析xml文件
     * */
    private final Thread loadThread = new Thread() {
        @Override
        public void run() {
            // 定义输入流加载中国地图的xml文件
            InputStream inputStream = context.getResources().openRawResource(R.raw.china);

            itemList = new ArrayList<>();
            try {
                // 取得DocumentBuilderFactory实例
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
                Document doc = builder.parse(inputStream);
                // 获取xml根节点
                Element rootElement = doc.getDocumentElement();
                // 获取根节点下的path节点集合
                NodeList items = rootElement.getElementsByTagName("path");

                // 定义四个边界点，为了确定地图所占矩形的位置
                float left = -1;
                float right = -1;
                float top = -1;
                float bottom = -1;

                // 遍历所有的path节点
                for (int x = 0; x < items.getLength(); x++) {
                    // 获取每一个path节点
                    Element element = (Element) items.item(x);
                    // 获取到path节点中的android:pathData属性值
                    String pathData = element.getAttribute("android:pathData");
                    // 将path字符串转为path对象
                    Path path = PathParser.createPathFromPathData(pathData);
                    ProvinceItem provinceItem = new ProvinceItem(path);
                    // 收集所有的path对象
                    itemList.add(provinceItem);


                    // 获取每个省份的边界
                    RectF rectF = new RectF();
                    path.computeBounds(rectF, true); // 获取path的边界并赋值给rect
                    // 确定整个地图的边界
                    // 遍历取出所有path的left的最小值
                    left = left == -1 ? rectF.left : Math.min(rectF.left, left);
                    // 遍历取出所有path的top的最小值
                    top = top == -1 ? rectF.top : Math.min(rectF.top, top);
                    // 遍历取出所有path的right的最大值
                    right = right == -1 ? rectF.right : Math.max(rectF.right, right);
                    // 遍历取出所有path的bottom的最大值
                    bottom = bottom == -1 ? rectF.bottom : Math.max(rectF.bottom, bottom);
                }
                mapRectf = new RectF(left, top, right, bottom);
                handler.sendEmptyMessage(1);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (itemList == null) {
                return;
            }
            int totalNumber = itemList.size();
            for (int i = 0; i < totalNumber; i++) {
                int color = Color.WHITE;
                int flag = i % 4;
                switch (flag) {
                    case 1:
                        color = colorArray[0];
                        break;
                    case 2:
                        color = colorArray[1];
                        break;
                    case 3:
                        color = colorArray[2];
                        break;
                    default:
                        color = Color.CYAN;
                        break;
                }
                //将颜色设置给每个省份的封装对象
                itemList.get(i).setDrawColor(color);
            }
            postInvalidate();
        }
    };


}
