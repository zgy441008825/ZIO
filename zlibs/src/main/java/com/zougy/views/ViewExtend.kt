package com.zougy.views

import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.widget.Toast
import com.zougy.views.ViewClickDelay.hash
import com.zougy.views.ViewClickDelay.lastClickTime

/**
 * Description:定义View的扩展方法<br>
 * Author:邹高原<br>
 * Date:11/19 0019<br>
 * Email:441008824@qq.com
 */

object ViewClickDelay {
    var hash: Int = 0
    var lastClickTime: Long = 0
}

/**
 * 扩展View的点击事件，防止重复点击。此方法会校验View的hashCode，只有相同的控件才处理防抖。
 */
fun <T : View> T.onClickOnShake(block: (T) -> Unit, time: Long = 1000) {
    setOnClickListener {
        if (this.hashCode() != hash) {
            hash = hashCode()
            lastClickTime = System.currentTimeMillis()
            block(it as T)
        } else {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastClickTime > time) {
                lastClickTime = System.currentTimeMillis()
                block(it as T)
            }
        }
    }
}

/**
 * 扩展View的点击事件，防止重复点击。此方法不校验View的hashCode，即任何连续点击不同控件也会进行防抖处理。
 */
fun <T : View> T.onClickOnSinge(block: (T) -> Unit, time: Long = 1000) {
    setOnClickListener {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime > time) {
            lastClickTime = System.currentTimeMillis()
            block(it as T)
        }
    }
}

/**
 * 扩展View的显示和隐藏，同时加载动画。
 */
fun View.visibilityWithAnimation(visibility: Int, animation: Animation) {
    this.visibility = visibility
    startAnimation(animation)
}

fun Context.toast(msgId: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msgId, duration).show()
}

fun Context.toast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, duration).show()
}