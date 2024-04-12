package com.zougy.receiver

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.zougy.commons.ZLog
import com.zougy.ui.activity.ZLifecycleBaseActivity
import com.zougy.ui.callback.SimpleLifecycleCallback
import java.lang.reflect.Method

class ReceiverMgr {

    private var broadcastReceiverMethod: Method? = null
    private var activity: ZLifecycleBaseActivity? = null
    private var registerReceiver = false

    fun observe(activity: ZLifecycleBaseActivity) {
        this.activity = activity
        val methods = activity::class.java.declaredMethods
        methods.filter {
            it.isAnnotationPresent(RegisterReceiver::class.java)
        }.forEach {
            broadcastReceiverMethod = it
            it.getAnnotation(RegisterReceiver::class.java)?.actions.let { act ->
                if (act == null) return@forEach
                val filter = IntentFilter()
                act.forEach { a -> filter.addAction(a) }
                activity.registerReceiver(receiver, filter)
                activity.lifecycleCallback = callback
                registerReceiver = true
                return@forEach
            }
        }
    }


    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            broadcastReceiverMethod?.let {
                it.isAccessible = true
                it.invoke(activity, intent)
            }
        }
    }

    private val callback = object : SimpleLifecycleCallback() {

        override fun onDestroy(activity: Activity) {
            activity.let {
                if (registerReceiver) {
                    it.unregisterReceiver(receiver)
                    registerReceiver = false
                }
            }
        }

    }
}