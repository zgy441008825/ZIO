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
     * TAG的后缀 一般可以用来设置版本号
     */
    var tagSuffix = ""

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
        return "${tag}_$tagSuffix"
    }

    fun d(tag: String = TAG, msg: Any) {
        if (enableLog && logLevel <= LoggerLevel.LEVEL_D) {
            iLogger.d(getTag(tag), if (showMethodInfo) "${getMethodInfo()} $msg" else "$msg")
        }
    }

    private fun print(tag: String = TAG, msg: String, level: Int = LoggerLevel.LEVEL_D) {
        when (level) {
            LoggerLevel.LEVEL_D -> {
                d(tag, msg)
            }

            LoggerLevel.LEVEL_I -> {
                i(tag, msg)
            }

            LoggerLevel.LEVEL_E -> {
                e(tag, msg, null)
            }

            LoggerLevel.LEVEL_W -> {
                w(tag, msg, null)
            }
        }
    }

    fun extend(tag: String = TAG, msg: Any, level: Int = LoggerLevel.LEVEL_D) {
        if (enableLog && logLevel <= LoggerLevel.LEVEL_D) {
            when (msg) {
                is Array<*> -> {
                    print(tag, msg.contentToString())
                }

                is List<*> -> {
                    if (msg.isEmpty()) {
                        print(tag, "msg is null")
                        return
                    }
                    print(tag, "start print list ${msg[0]?.javaClass}")
                    msg.forEach {
                        iLogger.d(tag, if (showMethodInfo) "${getMethodInfo()} $it" else "$it")
                        print(tag, it.toString())
                    }
                    print(tag, "end print list ${msg[0]?.javaClass}")
                }

                else -> {
                    print(tag, msg.toString())
                }
            }
        }
    }

    /**
     * 会识别msg类型，将数组或者列表的内容展开打印,如果是数组则调用contentToString方法返回string，如果是list，则分行打印列表内容。
     */
    fun extendD(tag: String = TAG, msg: Any) {
        extend(tag, msg)
    }

    fun i(tag: String = TAG, msg: Any) {
        if (enableLog && logLevel <= LoggerLevel.LEVEL_I) {
            iLogger.i(getTag(tag), if (showMethodInfo) "${getMethodInfo()} $msg" else "$msg")
        }
    }

    /**
     * 会识别msg类型，将数组或者列表的内容展开打印,如果是数组则调用contentToString方法返回string，如果是list，则分行打印列表内容。
     */
    fun extendI(tag: String = TAG, msg: Any) {
        extend(tag, msg, LoggerLevel.LEVEL_I)
    }


    fun w(tag: String = TAG, msg: Any, exception: Throwable?) {
        if (enableLog && logLevel <= LoggerLevel.LEVEL_I) {
            iLogger.w(getTag(tag), if (showMethodInfo) "${getMethodInfo()} $msg" else "$msg", exception)
        }
    }

    fun e(tag: String = TAG, msg: Any, exception: Throwable? = null) {
        if (enableLog && logLevel <= LoggerLevel.LEVEL_I) {
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