package com.zougy.views

import android.view.View
import com.zougy.views.ViewClickDelay.hash
import com.zougy.views.ViewClickDelay.lastClickTime

/**
 * Description:<br>
 * Author:邹高原<br>
 * Date:11/19 0019<br>
 * Email:441008824@qq.com
 */

object ViewClickDelay {
    var hash: Int = 0
    var lastClickTime: Long = 0
}

/**
 * 扩展View的点击事件，防止重复点击。
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