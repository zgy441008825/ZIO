package com.zougy.log

import android.text.TextUtils
import android.util.Log

/**
 * @Description:
 * Log类，用于打印日志。
 *
 * 可通过设置[iLogger]的方法注入自己的实现类，默认使用[ZLogger]类打印
 *
 * @CreateTime:2023/7/25 23:23
 * @Author:ZouGaoYuan
 * @Email:441008825@qq.com
 */
object LogUtils {

    private const val TAG = "LogUtils"

    /**
     * 打印日志的接口，可通过注入的方式设置自己的实现类
     */
    var iLogger: ILogger = ZLogger()

    /**
     * 日志保存接口
     */
    var iLogSaver: ILogSaver? = null

    /**
     * 设置日志打印的等级
     */
    var logLevel = LoggerLevel.LEVEL_D

    /**
     * 日志总开关
     */
    var enableLog = true

    /**
     * 是否跟踪显示方法进程相关信息
     */
    var showMethodInfo = true

    /**
     * TAG的前缀 一般设置应用的模块名称
     */
    var tagPrefix = ""

    /**
     * 获取方法信息
     */
    private fun getMethodInfo(): String {
        val sts = Thread.currentThread().stackTrace ?: return ""
        for (st in sts) {
            if (st.isNativeMethod) {
                continue
            }
            if (st.className == Thread::class.java.name) {
                continue
            }
            if (st.className == LogUtils::class.java.name) {
                continue
            }
            return "[${st.className} :${st.methodName} :${st.lineNumber}]:"
        }
        return ""
    }

    private fun getTag(tag: String = TAG): String {
        return "$tagPrefix $tag"
    }

    fun d(tag: String = TAG, msg: Any) {
        if (enableLog && logLevel <= LoggerLevel.LEVEL_D) {
            iLogger.d(getTag(tag), if (showMethodInfo) "${getMethodInfo()} $msg" else "$msg")
        }
    }

    fun i(tag: String = TAG, msg: Any) {
        if (enableLog && logLevel <= LoggerLevel.LEVEL_I) {
            iLogger.i(getTag(tag), if (showMethodInfo) "${getMethodInfo()} $msg" else "$msg")
        }
    }

    fun w(tag: String = TAG, msg: Any, exception: Throwable? = null) {
        if (enableLog && logLevel <= LoggerLevel.LEVEL_W) {
            iLogger.w(getTag(tag), if (showMethodInfo) "${getMethodInfo()} $msg" else "$msg", exception)
        }
    }

    fun e(tag: String = TAG, msg: Any, exception: Throwable? = null) {
        if (enableLog && logLevel <= LoggerLevel.LEVEL_E) {
            iLogger.e(getTag(tag), if (showMethodInfo) "${getMethodInfo()} $msg" else "$msg", exception)
        }
    }

}

private class ZLogger : ILogger {

    companion object {
        private const val TAG = "ZLogger"
    }

    override fun d(tag: String?, msg: String) {
        Log.d(if (TextUtils.isEmpty(tag)) TAG else tag, msg)
    }

    override fun i(tag: String?, msg: String) {
        Log.i(if (TextUtils.isEmpty(tag)) TAG else tag, msg)
    }

    override fun w(tag: String?, msg: String, e: Throwable?) {
        e?.apply {
            Log.w(TAG, msg, e)
        } ?: {
            Log.w(TAG, msg)
        }
    }

    override fun e(tag: String?, msg: String, e: Throwable?) {
        e?.apply {
            Log.e(TAG, msg, e)
        } ?: {
            Log.e(TAG, msg)
        }
    }
}