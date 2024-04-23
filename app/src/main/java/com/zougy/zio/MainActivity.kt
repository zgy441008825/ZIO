package com.zougy.zio

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.zougy.tools.countDown
import com.zougy.ui.views.onClickOnSinge


class MainActivity : BaseActivity() {

    companion object {
        const val TAG = "MainActivity"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val acMainTvCountDown = findViewById<TextView>(R.id.acMainTvCountDown)
        acMainTvCountDown.onClickOnSinge {
            10.countDown(1, {
                acMainTvCountDown.text = it.toString()
            }, {
                acMainTvCountDown.text = "倒计时结束"
            })
        }
    }

}
