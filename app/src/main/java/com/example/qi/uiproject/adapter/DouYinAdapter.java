package com.example.qi.uiproject.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qi.uiproject.R;

public class DouYinAdapter extends RecyclerView.Adapter<DouYinAdapter.DouYinViewHolder> {

    private final Context context;
    private int[] images = {R.mipmap.img_video_1, R.mipmap.img_video_2};
    private int[] videos = {R.raw.video_1, R.raw.video_2};

    public DouYinAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public DouYinViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_pager, parent, false);
        return new DouYinViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DouYinViewHolder holder, int position) {
        holder.img_thumb.setImageResource(images[position % 2]);
        holder.videoView.setVideoURI(Uri.parse("android.resource://" + context.getPackageName() + "/" + videos[position % 2]));
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    public static class DouYinViewHolder extends RecyclerView.ViewHolder {
        ImageView img_thumb;
        VideoView videoView;
        ImageView img_play;
        RelativeLayout rootView;

        public DouYinViewHolder(View itemView) {
            super(itemView);
            img_thumb = itemView.findViewById(R.id.img_thumb);
            videoView = itemView.findViewById(R.id.video_view);
            img_play = itemView.findViewById(R.id.img_play);
            rootView = itemView.findViewById(R.id.root_view);
        }


    }

}
