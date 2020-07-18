package com.zougy.zio

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.zougy.commons.ZLog
import com.zougy.receiver.ReceiverMgr
import com.zougy.receiver.RegisterReceiver

class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ReceiverMgr.observe(this)

        sendBroadcast(Intent("888"))
    }

    @RegisterReceiver(actions = arrayOf("77777", "888"))
    fun onReceiver(act: Intent) {
        ZLog.d(act.action)
    }

}
