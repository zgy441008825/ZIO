package com.zougy.log

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

    var TAG = "LogUtils"

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

    fun d(tag: String? = TAG, msg: Any, showMethodInfo: Boolean = true) {
        if (enableLog && logLevel <= LoggerLevel.LEVEL_D) {
            iLogger.d(tag, if (showMethodInfo) "${getMethodInfo()} $msg" else "$msg")
        }
    }

    /**
     * 会识别msg类型，将数组或者列表的内容展开打印,如果是数组则调用contentToString方法返回string，如果是list，则分行打印列表内容。
     */
    fun extendD(tag: String? = TAG, msg: Any, showMethodInfo: Boolean = true) {
        if (enableLog && logLevel <= LoggerLevel.LEVEL_D) {
            when (msg) {
                is Array<*> -> {
                    iLogger.d(tag, if (showMethodInfo) "${getMethodInfo()} ${msg.contentToString()}" else msg.contentToString())
                }

                is List<*> -> {
                    if (msg.isEmpty()) {
                        iLogger.d(tag, if (showMethodInfo) "${getMethodInfo()} msg is null" else "msg is null")
                    }
                    var showMsg = "start print list ${msg[0]?.javaClass}"
                    iLogger.d(tag, if (showMethodInfo) "${getMethodInfo()} $showMsg" else showMsg)
                    msg.forEach {
                        iLogger.d(tag, if (showMethodInfo) "${getMethodInfo()} $it" else "$it")
                    }
                    showMsg = "end print list ${msg[0]?.javaClass}"
                    iLogger.d(tag, if (showMethodInfo) "${getMethodInfo()} $showMsg" else showMsg)
                }

                else -> {
                    iLogger.d(tag, if (showMethodInfo) "${getMethodInfo()} $msg" else "$msg")
                }
            }
        }
    }

    fun i(tag: String? = TAG, msg: Any, showMethodInfo: Boolean = true) {
        if (enableLog && logLevel <= LoggerLevel.LEVEL_I) {
            iLogger.i(tag, if (showMethodInfo) "${getMethodInfo()} $msg" else "$msg")
        }
    }

    /**
     * 会识别msg类型，将数组或者列表的内容展开打印,如果是数组则调用contentToString方法返回string，如果是list，则分行打印列表内容。
     */
    fun extendI(tag: String? = TAG, msg: Any, showMethodInfo: Boolean = true) {
        if (enableLog && logLevel <= LoggerLevel.LEVEL_D) {
            when (msg) {
                is Array<*> -> {
                    iLogger.i(tag, if (showMethodInfo) "${getMethodInfo()} ${msg.contentToString()}" else msg.contentToString())
                }

                is List<*> -> {
                    if (msg.isEmpty()) {
                        iLogger.i(tag, if (showMethodInfo) "${getMethodInfo()} msg is null" else "msg is null")
                    }
                    var showMsg = "start print list ${msg[0]?.javaClass}"
                    iLogger.d(tag, if (showMethodInfo) "${getMethodInfo()} $showMsg" else showMsg)
                    msg.forEach {
                        iLogger.i(tag, if (showMethodInfo) "${getMethodInfo()} $it" else "$it")
                    }
                    showMsg = "end print list ${msg[0]?.javaClass}"
                    iLogger.i(tag, if (showMethodInfo) "${getMethodInfo()} $showMsg" else showMsg)
                }

                else -> {
                    iLogger.i(tag, if (showMethodInfo) "${getMethodInfo()} $msg" else "$msg")
                }
            }
        }
    }


    fun w(tag: String? = TAG, msg: Any, exception: Throwable?, showMethodInfo: Boolean = true) {
        if (enableLog && logLevel <= LoggerLevel.LEVEL_I) {
            iLogger.w(tag, if (showMethodInfo) "${getMethodInfo()} $msg" else "$msg", exception)
        }
    }

    fun e(tag: String? = TAG, msg: Any, exception: Throwable?, showMethodInfo: Boolean = true) {
        if (enableLog && logLevel <= LoggerLevel.LEVEL_I) {
            iLogger.e(tag, if (showMethodInfo) "${getMethodInfo()} $msg" else "$msg", exception)
        }
    }


}