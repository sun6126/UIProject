package com.example.qi.uiproject.ui

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationSet
import com.example.qi.uiproject.R
import kotlinx.android.synthetic.main.activity_animation.*

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
    }

    // 集合动画
    private fun startSetAnimation() {
        val translation = ObjectAnimator.ofFloat(
            tvTest,
            "translationX",
            -500f,
            tvTest.translationX
        )

        val alpha = ObjectAnimator.ofFloat(tvTest, "alpha", 0f, 1f)

        val rotation = ObjectAnimator.ofFloat(tvTest, "rotation", 0f, 360f)

        val animSet = AnimatorSet()
        animSet.play(alpha).with(rotation).after(translation)
        animSet.setDuration(400)
        animSet.start()
    }
}