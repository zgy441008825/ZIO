package com.zougy.log

import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.log

/**
 * @Description:
 *
 * @CreateTime:2023/12/8 0:13
 * @Author:ZouGaoYuan
 * @Email:441008825@qq.com
 */
class ZLogSaver constructor(private var logRootDir: String) : ILogSaver {

    /**
     * 日志文件命名的前缀
     */
    private var logFilePrefix = "ZLog"

    /**
     * 日志文件命名的时间格式，如果不为空，日志文件的名称为 前缀_时间.后缀
     */
    private var logFileTimeFormat = SimpleDateFormat("yyyyMMddHHmmsss", Locale.getDefault())

    /**
     * 日志文件名称的后缀
     */
    private var logFileSuffix = "log"

    /**
     * 格式化log的时间
     */
    private var logTimeFormat = SimpleDateFormat("MM:dd HH:mm:sss", Locale.getDefault())

    /**
     * 单个日志文件的最大值
     */
    private var logFileMaxSize: Long = 1024 * 1024 * 1024

    /**
     * 日志目录的前缀
     */
    private var logDirPrefix = "ZLog"

    /**
     * 日志目录的最大文件个数
     */
    private var logDirFileMax = 10

    /**
     * 日志目录的格式化时间
     */
    private var logDirTimeFormat = SimpleDateFormat("yyyy-MM-dd-HH-mm", Locale.getDefault())

    /**
     * 当前保存日志的目录
     */
    private var curLogDir: File? = null

    /**
     * 当前保存日志的文件
     */
    private var curLogFile: File? = null


    override fun setTimeFormat(format: SimpleDateFormat) {
        logTimeFormat = format
    }

    override fun setTimeFormat(format: String) {
        logTimeFormat = SimpleDateFormat(format, Locale.getDefault())
    }

    override fun setFileRootDir(dir: String) {
        logRootDir = dir
    }

    override fun setLogFileFormat(prefix: String, timeFormat: String, suffix: String) {
        logFilePrefix = prefix
        logFileTimeFormat = SimpleDateFormat(timeFormat, Locale.getDefault())
        logFileSuffix = suffix
    }

    override fun setLogFileMaxSize(size: Long) {
        logFileMaxSize = size
    }

    override fun setLogDirFormat(prefix: String, timeFormat: String, fileCnt: Int) {
        logDirPrefix = prefix
        logDirTimeFormat = SimpleDateFormat(timeFormat, Locale.getDefault())
        logDirFileMax = fileCnt
    }

    override fun saveLog(msg: String) {


    }

    /**
     * 判断当前目录是否达到文件个数限制
     *
     * @return true: 还没有达到限制，  false: 已经达到限制
     */
    private fun checkDirFileSize(): Boolean {
        curLogDir?.apply {
            listFiles()?.apply {
                if (this.size < logDirFileMax)
                    return true
            }
        }
        return false
    }

    /**
     * 创建日志目录
     */
    private fun createLogDir(): Boolean {
        val fileName = "$logDirPrefix-${logDirTimeFormat.format(Date(System.currentTimeMillis()))}"
        val logDir = File(logRootDir, fileName)
        logDir.mkdirs()
        if (logDir.exists()) {
            curLogDir = logDir
            return true
        }
        return false
    }

    private fun createLogFile(): Boolean {
        if (curLogDir == null || !curLogDir!!.exists()) return false
        if (!checkDirFileSize() && !createLogDir()) return false

        val fileName = "$logFilePrefix${logFileTimeFormat.format(Date(System.currentTimeMillis()))}.$logFileSuffix"
        val logFile = File(curLogDir, fileName)
        if (logFile.createNewFile()) {
            curLogFile = logFile
            return true
        }
        return false
    }
}