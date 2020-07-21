package com.zougy.zio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zougy.commons.ZLog
import com.zougy.receiver.ReceiverMgr
import com.zougy.receiver.RegisterReceiver
import com.zougy.ui.activity.ZLifecycleBaseActivity

class MainActivity2 : ZLifecycleBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        ReceiverMgr().observe(this)

        sendBroadcast(Intent("000"))
    }

    @RegisterReceiver(actions = arrayOf("000", "999"))
    fun onReceiver(act: Intent) {
        ZLog.d(act.action)
    }
}