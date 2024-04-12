package com.zougy.zio

import android.content.Intent
import android.os.Bundle
import com.zougy.log.LogUtils
import com.zougy.receiver.ReceiverMgr
import com.zougy.receiver.RegisterReceiver
import com.zougy.ui.activity.ZLifecycleBaseActivity

class MainActivity2 : ZLifecycleBaseActivity() {

    companion object {
        private const val TAG = "MainActivity2"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        ReceiverMgr().observe(this)

        sendBroadcast(Intent("000"))
    }

    @RegisterReceiver(actions = arrayOf("000", "999"))
    fun onReceiver(act: Intent) {
        LogUtils.i(TAG, "intent:$act")
    }
}