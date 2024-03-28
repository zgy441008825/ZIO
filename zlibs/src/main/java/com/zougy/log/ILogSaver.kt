package com.zougy.log

import java.text.SimpleDateFormat

/**
 * @Description:
 * 保存日志接口
 * @CreateTime:2023/12/8 0:01
 * @Author:ZouGaoYuan
 * @Email:441008825@qq.com
 */
interface ILogSaver {

    /**
     * 保存日志
     */
    fun saveLog(msg: String)

    /**
     * 设置日志的格式化时间
     */
    fun setTimeFormat(format: SimpleDateFormat)

    /**
     * 设置格式化时间
     */
    fun setTimeFormat(format: String)

    /**
     * 设备保存文件的根目录
     */
    fun setFileRootDir(dir: String)

    /**
     * 设置日志文件格式 后缀
     *
     * @param prefix 前缀
     * @param timeFormat 时间格式
     * @param suffix 后缀
     */
    fun setLogFileFormat(prefix: String, timeFormat: String = "yyyyMMddHHmmsss", suffix: String)

    /**
     * 设置单个日志文件最大限值
     */
    fun setLogFileMaxSize(size: Long)

    /**
     * 设置日志目录格式
     * @param prefix 前缀
     * @param timeFormat 时间格式
     * @param fileCnt 最大文件个数
     */
    fun setLogDirFormat(prefix: String, timeFormat: String = "yyyy-MM-dd-HH-mm", fileCnt: Int)
}