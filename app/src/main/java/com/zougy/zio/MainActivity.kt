package com.zougy.zio

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.zougy.commons.ZLog
import com.zougy.receiver.ReceiverMgr
import com.zougy.receiver.RegisterReceiver
import com.zougy.ui.activity.ZLifecycleBaseActivity

class MainActivity : BaseActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}
