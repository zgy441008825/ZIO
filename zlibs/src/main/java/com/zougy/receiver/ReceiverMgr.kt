package com.zougy.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import com.zougy.commons.ZLog
import java.lang.reflect.Method

object ReceiverMgr {

    private var broadcastReceiverMethod: Method? = null
    private var activity: AppCompatActivity? = null

    fun observe(activity: AppCompatActivity) {
        this.activity = activity
        val methods = activity::class.java.declaredMethods
        ZLog.d(methods)
        methods.filter {
            it.isAnnotationPresent(RegisterReceiver::class.java)
        }.forEach {
            ZLog.d("RegisterReceiver $it")
            broadcastReceiverMethod = it
            it.getAnnotation(RegisterReceiver::class.java)?.actions.let { act ->
                if (act == null) return@forEach
                val filter = IntentFilter()
                act.forEach { a -> filter.addAction(a) }
                activity.registerReceiver(receiver, filter)
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

}