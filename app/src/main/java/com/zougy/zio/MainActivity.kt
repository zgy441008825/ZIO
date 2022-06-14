package com.zougy.zio

import android.os.Bundle
import com.zougy.commons.ZLog

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        ZLog.d("onCreate")
    }
}
