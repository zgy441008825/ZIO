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

    private var isDebug = true

    fun setDebug(debug: Boolean = true) {
        isDebug = debug
    }

    fun isDebug(): Boolean {
        return isDebug
    }

    private inline fun getMethodInfo(): String {
        val element: StackTraceElement = Throwable().stackTrace[2]
        val className = element.className.substring(element.className.lastIndexOf(".") + 1)
        val methodName = element.methodName
        val lineNumber = element.lineNumber
        return "$className $methodName L:$lineNumber "
    }

    fun d(tag: String = "ZLog", msg: Any? = null, showMethodInfo: Boolean = true) {
        if (isDebug) Log.d(tag, if (showMethodInfo) "${getMethodInfo()} $msg" else "$msg")
    }

    fun e(tag: String = "ZLog", msg: Any, exception: Exception?, showMethodInfo: Boolean = true) {
        if (isDebug) Log.e(tag, if (showMethodInfo) "${getMethodInfo()} $msg" else msg.toString(), exception)
    }

    fun i(tag: String = "ZLog", msg: Any? = null, showMethodInfo: Boolean = true) {
        if (isDebug) Log.i(tag, if (showMethodInfo) "${getMethodInfo()} $msg" else msg.toString())
    }

    fun v(tag: String = "ZLog", msg: Any? = null, showMethodInfo: Boolean = true) {
        if (isDebug) Log.v(tag, if (showMethodInfo) "${getMethodInfo()} $msg" else msg.toString())
    }

    fun w(tag: String = "ZLog", msg: Any, exception: Exception?, showMethodInfo: Boolean = true) {
        if (isDebug) Log.w(tag, if (showMethodInfo) "${getMethodInfo()} $msg" else msg.toString(), exception)
    }


}