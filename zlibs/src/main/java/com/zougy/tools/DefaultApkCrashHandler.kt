package com.zougy.tools

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.text.TextUtils
import android.util.Log
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter

class DefaultApkCrashHandler private constructor(val context: Context) : Thread.UncaughtExceptionHandler {

    private val exceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
    private var crashLogSavePath: String? = null
    private var exceptionToDo: (Throwable.() -> Unit)? = null

    companion object {

        @Volatile
        private var instance: DefaultApkCrashHandler? = null

        fun getInstance(context: Context): DefaultApkCrashHandler {
            if (instance == null) {
                synchronized(DefaultApkCrashHandler::class.java) {
                    if (instance == null) {
                        instance = DefaultApkCrashHandler(context)
                    }
                }
            }
            return instance!!
        }
    }

    init {
        Log.d("ZLog CrashHandler", "init")
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    fun init(fileSavePath: String, block: (Throwable.() -> Unit)? = null) {
        crashLogSavePath = fileSavePath
        exceptionToDo = block
    }

    override fun uncaughtException(t: Thread, e: Throwable) {
        Log.d("ZLog CrashHandler", "uncaughtException: $e")
        saveCrashLog(e)
        exceptionToDo?.invoke(e)
        exceptionHandler.uncaughtException(t, e)
    }

    private fun saveCrashLog(e: Throwable) {
        Log.d("ZLog CrashHandler", "saveCrashLog: $e")
        getSaveFile()?.let {
            val sb = getSystemAndApkInfo()
            sb.append("\nThrows Info:\n")
            val writer = FileWriter(it)
            val print = PrintWriter(writer)
            print.println(sb.toString())
            var throws = e.cause
            if (throws != null) {
                while (throws != null) {
                    throws.printStackTrace(print)
                    throws = throws.cause
                }
            }
            writer.close()
            print.close()
        }
    }

    private fun getSaveFile(): File? {
        if (TextUtils.isEmpty(crashLogSavePath)) return null
        val parentFile = File(crashLogSavePath)
        if (!parentFile.exists() && !parentFile.mkdirs()) return null
        val logFile = File(parentFile, "crashLog-${System.currentTimeMillis().formatDate("yyyy-MM-dd-HH-mm-ss")}.log")
        return if (logFile.createNewFile()) logFile
        else null
    }

    private fun getSystemAndApkInfo(): StringBuilder {
        val sb = StringBuilder()
        val pkgMgr = context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_ACTIVITIES)
        sb.append("APK Info:\n")
        sb.append("versionName:${pkgMgr.versionName}\n")
        sb.append("versionCode:${pkgMgr.versionCode}\n\n")
        sb.append("SystemInfo:\n")
        val fiedls = Build::class.java.declaredFields
        for (f in fiedls) {
            try {
                f.isAccessible = true
                sb.append("${f.name}:${f.get(null)}\n")
            } catch (e: Exception) {
            }
        }
        return sb
    }
}