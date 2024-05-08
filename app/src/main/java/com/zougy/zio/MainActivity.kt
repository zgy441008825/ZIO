package com.zougy.zio

import android.os.Bundle
import com.zougy.ui.views.ProgressView


class MainActivity : BaseActivity() {

    companion object {
        const val TAG = "MainActivity"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}
