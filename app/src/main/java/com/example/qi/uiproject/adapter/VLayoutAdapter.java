package com.example.qi.uiproject.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.example.qi.uiproject.R;

import java.util.ArrayList;
import java.util.HashMap;

public class VLayoutAdapter extends DelegateAdapter.Adapter<VLayoutAdapter.VLayoutViewHolder> {
    // 数据源
    private ArrayList<HashMap<String,Object>> listItem;
    private Context context;
    // 数据总数量
    private int count;
    // LayoutHelper对象
    private LayoutHelper helper;

    public VLayoutAdapter(ArrayList<HashMap<String, Object>> listItem, Context context, int count, LayoutHelper helper) {
        this.listItem = listItem;
        this.context = context;
        this.count = count;
        this.helper = helper;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return helper;
    }

    @NonNull
    @Override
    public VLayoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_vlayout, null);
        return new VLayoutViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull VLayoutViewHolder holder, int position) {
        holder.imageView.setImageResource((int) listItem.get(position).get("image"));
        holder.textView.setText((String) listItem.get(position).get("text"));
    }

    @Override
    public int getItemCount() {
        return count;
    }

    public static class VLayoutViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        public VLayoutViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
        }
    }
}
