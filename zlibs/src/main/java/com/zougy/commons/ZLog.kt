package com.zougy.commons

import android.text.TextUtils
import android.util.Log
import com.zougy.commons.ZLog.TAG

/**
 * Description:<br>
 * Author:邹高原<br>
 * Date:04/01 0001<br>
 * Email:441008824@qq.com
 */

interface ILog {
    fun d(tag: String?, msg: String)
    fun i(tag: String?, msg: String)
    fun w(tag: String?, msg: String, exception: Throwable?)
    fun e(tag: String?, msg: String, exception: Throwable?)
    fun v(tag: String?, msg: String, exception: Throwable?)
}

/**
 * 自定义Log实现<br>
 * 通过注入[ZLog.logger]注入自定义log实现
 */
object ZLog {

    private var isDebug = true

    var TAG = "ZLog"

    var logger: ILog = ZLogger()

    private inline fun getMethodInfo(): String {
        val sts = Thread.currentThread().stackTrace ?: return ""
        for (st in sts) {
            if (st.isNativeMethod) {
                continue
            }
            if (st.className == Thread::class.java.name) {
                continue
            }
            if (st.className == ZLog::class.java.name) {
                continue
            }
            return "[${st.className} :${st.methodName} :${st.lineNumber}]:"
        }
        return ""
    }

    fun enable(enable: Boolean) {
        isDebug = enable
    }

    fun d(msg: Any? = null, tag: String = TAG, showMethodInfo: Boolean = true) {
        if (isDebug) logger.d(tag, if (showMethodInfo) "${getMethodInfo()} $msg" else "$msg")
    }

    fun i(msg: Any? = null, tag: String = TAG, showMethodInfo: Boolean = true) {
        if (isDebug) logger.i(tag, if (showMethodInfo) "${getMethodInfo()} $msg" else msg.toString())
    }

    fun w(msg: Any, tag: String = TAG, exception: Throwable?, showMethodInfo: Boolean = true) {
        if (isDebug) logger.w(tag, if (showMethodInfo) "${getMethodInfo()} $msg" else msg.toString(), exception)
    }

    fun e(msg: Any, tag: String = TAG, exception: Throwable?, showMethodInfo: Boolean = true) {
        if (isDebug) logger.e(tag, if (showMethodInfo) "${getMethodInfo()} $msg" else msg.toString(), exception)
    }

    fun v(msg: Any? = null, tag: String = TAG, showMethodInfo: Boolean = true, exception: Throwable?) {
        if (isDebug) logger.v(tag, if (showMethodInfo) "${getMethodInfo()} $msg" else msg.toString(), exception)
    }

}

class ZLogger : ILog {

    override fun d(tag: String?, msg: String) {
        Log.d(if (TextUtils.isEmpty(tag)) TAG else tag, msg)
    }

    override fun i(tag: String?, msg: String) {
        Log.i(if (TextUtils.isEmpty(tag)) TAG else tag, msg)
    }

    override fun w(tag: String?, msg: String, exception: Throwable?) {
        if (exception == null) {
            Log.w(if (TextUtils.isEmpty(tag)) TAG else tag, msg)
        } else {
            Log.w(if (TextUtils.isEmpty(tag)) TAG else tag, msg, exception)
        }
    }

    override fun e(tag: String?, msg: String, exception: Throwable?) {
        if (exception == null) {
            Log.e(if (TextUtils.isEmpty(tag)) TAG else tag, msg)
        } else {
            Log.e(if (TextUtils.isEmpty(tag)) TAG else tag, msg, exception)
        }
    }

    override fun v(tag: String?, msg: String, exception: Throwable?) {
        if (exception == null) {
            Log.v(if (TextUtils.isEmpty(tag)) TAG else tag, msg)
        } else {
            Log.v(if (TextUtils.isEmpty(tag)) TAG else tag, msg, exception)
        }
    }

}