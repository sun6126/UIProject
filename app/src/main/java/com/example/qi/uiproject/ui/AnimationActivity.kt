package com.example.qi.uiproject.ui

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationSet
import com.example.qi.uiproject.R
import kotlinx.android.synthetic.main.activity_animation.*
import javax.security.auth.login.LoginException

class AnimationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animation)
        initListener()
    }

    private fun initListener() {
        btnAnimationSet.setOnClickListener { // 播放集合动画
            startSetAnimation()
        }
        btnScroll.setOnClickListener {
//            btnAnimationSet.translationX = 60f
            btnAnimationSet.translationY = btnAnimationSet.translationY + 60f
            Log.e("sunbaoqi",btnAnimationSet.x .toString()+ "     "+ btnAnimationSet.y) // translationX/Y 向右移是正，向下移是正
//            btnAnimationSet.scrollBy(btnAnimationSet.x.toInt(),20) // scrollX和scrollY 移动内容，向上移是正，向下是负，向左移是正，向右移是负
        }
    }

    // 集合动画
    private fun startSetAnimation() {
        val translation = ObjectAnimator.ofFloat(
            tvTest,
            "translationX",
            -500f,
            500f
        )

        val alpha = ObjectAnimator.ofFloat(tvTest, "alpha", 0f, 1f)

        val rotation = ObjectAnimator.ofFloat(tvTest, "rotation", 0f, 360f)

        val animSet = AnimatorSet()
        animSet.play(alpha).with(rotation).after(translation)
        animSet.setDuration(400)
        animSet.start()
    }
}