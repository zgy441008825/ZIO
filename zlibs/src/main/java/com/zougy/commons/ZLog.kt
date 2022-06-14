package com.zougy.commons

import android.util.Log
import java.lang.Exception

/**
 * Description:<br>
 * Author:邹高原<br>
 * Date:04/01 0001<br>
 * Email:441008824@qq.com
 */

object ZLog {

    var isDebug = true

    var TAG = "ZLog"

    private inline fun getMethodInfo(): String {
        val element: StackTraceElement = Throwable().stackTrace[2]
        val className = element.className.substring(element.className.lastIndexOf(".") + 1)
        val methodName = element.methodName
        val lineNumber = element.lineNumber
        return "[$className:$methodName $lineNumber]:"
    }

    fun d(msg: Any? = null, tag: String = TAG, showMethodInfo: Boolean = true) {
        if (isDebug) Log.d(tag, if (showMethodInfo) "${getMethodInfo()} $msg" else "$msg")
    }

    fun e(msg: Any, tag: String = TAG, exception: Throwable?, showMethodInfo: Boolean = true) {
        if (isDebug) Log.e(tag, if (showMethodInfo) "${getMethodInfo()} $msg" else msg.toString(), exception)
    }

    fun i(msg: Any? = null, tag: String = TAG, showMethodInfo: Boolean = true) {
        if (isDebug) Log.i(tag, if (showMethodInfo) "${getMethodInfo()} $msg" else msg.toString())
    }

    fun v(msg: Any? = null, tag: String = TAG, showMethodInfo: Boolean = true) {
        if (isDebug) Log.v(tag, if (showMethodInfo) "${getMethodInfo()} $msg" else msg.toString())
    }

    fun w(msg: Any, tag: String = TAG, exception: Throwable?, showMethodInfo: Boolean = true) {
        if (isDebug) Log.w(tag, if (showMethodInfo) "${getMethodInfo()} $msg" else msg.toString(), exception)
    }


}