package com.example.qi.uiproject.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Switch
import com.example.qi.uiproject.R
import kotlinx.android.synthetic.main.activity_material_test.*

class MaterialTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_material_test)
        initListener()
    }

    private fun initListener() {
        btnObserable1.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_MOVE -> {
                    v.x = event.rawX - v.width/2
                    v.y = event.rawY - v.height/2
                    return@setOnTouchListener true
                }
                else -> return@setOnTouchListener false
            }

        }
    }
}
