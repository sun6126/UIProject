package com.example.qi.uiproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.qi.uiproject.ui.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initListener()

    }

    private fun initListener() {
        btnStartAnimationActivity.setOnClickListener {
            startActivity(Intent(MainActivity@ this, AnimationActivity::class.java))
        }

        btnStartMaterialActivity.setOnClickListener {
            startActivity(Intent(MainActivity@ this, MaterialTestActivity::class.java))
        }

        btnStartMyAttrsActivity.setOnClickListener {
            startActivity(Intent(MainActivity@ this, MyAttrsActivity::class.java))
        }

        btnPathMeasureActivity.setOnClickListener {
            startActivity(Intent(MainActivity@ this, PathMeasureActivity::class.java))
        }

        btnQuadToActivity.setOnClickListener {
            startActivity(Intent(MainActivity@ this, QuadToActivity::class.java))
        }

        btnCubicActivity.setOnClickListener {
            startActivity(Intent(MainActivity@ this, CubicActivity::class.java))
        }

        btnGiftActivity.setOnClickListener {
            startActivity(Intent(MainActivity@ this, GiftActivity::class.java))
        }

        btnNestedLinearLayoutActivity.setOnClickListener {
            startActivity(Intent(MainActivity@ this, NestedLinearLayoutActivity::class.java))
        }

        btnSplashActivity.setOnClickListener {
            startActivity(Intent(MainActivity@ this, SplashActivity::class.java))
        }

        btnVLayoutActivity.setOnClickListener {
            startActivity(Intent(MainActivity@ this, VLayoutActivity::class.java))
        }

        btnMapActivity.setOnClickListener {
            startActivity(Intent(MainActivity@ this, MapActivity::class.java))
        }

        btnDouYinActivity.setOnClickListener {
            startActivity(Intent(MainActivity@ this, DouYinActivity::class.java))
        }

        btnLoadMoreActivity.setOnClickListener {
            startActivity(Intent(MainActivity@ this, LoadMoreRecyclerViewActivity::class.java))
        }

    }
}
