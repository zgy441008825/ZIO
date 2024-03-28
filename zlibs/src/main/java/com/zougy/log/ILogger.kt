package com.zougy.log

/**
 * @Description:
 *
 * log接口
 *
 * @CreateTime:2023/7/25 23:05
 * @Author:ZouGaoYuan
 * @Email:441008825@qq.com
 */
interface ILogger {

    /**
     * 打印D级log 可指定tag，为null的话使用默认的tag
     */
    fun d(tag: String? = null, msg: String)

    /**
     * 打印I级log 可指定tag，为null的话使用默认的tag
     */
    fun i(tag: String? = null, msg: String)

    /**
     * 打印W级log 可指定tag，为null的话使用默认的tag
     */
    fun w(tag: String? = null, msg: String, e: Throwable? = null)

    /**
     * 打印E级log 可指定tag，为null的话使用默认的tag
     */
    fun e(tag: String? = null, msg: String, e: Throwable? = null)

}


/**
 * 日志等级
 */
object LoggerLevel {
    const val LEVEL_D = 0
    const val LEVEL_I = 1
    const val LEVEL_W = 2
    const val LEVEL_E = 3
}