package com.example.qi.uiproject.ui

import android.annotation.TargetApi
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.OrientationHelper
import com.example.qi.uiproject.R
import com.example.qi.uiproject.adapter.DouYinAdapter
import com.example.qi.uiproject.layoutManager.DouYinLayoutManager
import com.example.qi.uiproject.listener.OnViewPagerListener
import kotlinx.android.synthetic.main.activity_dou_yin.*

class DouYinActivity : AppCompatActivity() {

    val douYinLayoutManager = DouYinLayoutManager(this,OrientationHelper.VERTICAL,false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dou_yin)
        initView()
        initListener()
    }

    private fun initListener() {
        douYinLayoutManager.setViewPagerListener(object :
            OnViewPagerListener {
            override fun onPageRelease(itemView: View?) {
                releaseVideo(itemView!!);
            }

            override fun onPageSelected(itemView: View?) {
                playVideo(itemView!!)
            }

        })
    }

    private fun initView() {
        douYinRecyclerView.layoutManager = douYinLayoutManager
        douYinRecyclerView.adapter = DouYinAdapter(this)
    }

    /**
     * 停止播放
     * @param itemView
     */
    private fun releaseVideo(itemView: View) {
        val videoView = itemView.findViewById<VideoView>(R.id.video_view)
        val imgThumb =
            itemView.findViewById<ImageView>(R.id.img_thumb)
        val imgPlay =
            itemView.findViewById<ImageView>(R.id.img_play)
        videoView.stopPlayback()
        imgThumb.animate().alpha(1f).start()
        imgPlay.animate().alpha(0f).start()
    }

    /**
     * 开始播放
     * @param itemView
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun playVideo(itemView: View) {
        val videoView = itemView.findViewById<VideoView>(R.id.video_view)
        val imgPlay =
            itemView.findViewById<ImageView>(R.id.img_play)
        val imgThumb =
            itemView.findViewById<ImageView>(R.id.img_thumb)
        val rootView = itemView.findViewById<RelativeLayout>(R.id.root_view)
        val mediaPlayer = arrayOfNulls<MediaPlayer>(1)
        videoView.start()
        videoView.setOnInfoListener { mp, what, extra ->
            mediaPlayer[0] = mp
            mp.isLooping = true
            imgThumb.animate().alpha(0f).setDuration(200).start()
            false
        }
        videoView.setOnPreparedListener { }
        imgPlay.setOnClickListener(object : View.OnClickListener {
            var isPlaying = true
            override fun onClick(v: View) {
                isPlaying = if (videoView.isPlaying) {
                    imgPlay.animate().alpha(1f).start()
                    videoView.pause()
                    false
                } else {
                    imgPlay.animate().alpha(0f).start()
                    videoView.start()
                    true
                }
            }
        })
    }
}
