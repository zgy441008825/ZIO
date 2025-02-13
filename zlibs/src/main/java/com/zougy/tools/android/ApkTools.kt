package com.zougy.tools.android

import android.app.Activity
import android.app.ActivityManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageInstaller
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider.getUriForFile
import com.zougy.log.LogUtils
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.io.OutputStream

/**
 * Description:<br>
 * Author:邹高原<br>
 * Date:11/19 0019<br>
 * Email:441008824@qq.com
 */

fun Context.getPackageInfo(packageName: String = this.packageName): PackageInfo {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(PackageManager.GET_ACTIVITIES.toLong()))
    } else {
        packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
    }
}

fun Context.getApkVersionName(packageName: String = this.packageName): String {
    return getPackageInfo(packageName).versionName
}

fun Context.getApkVersionCode(packageName: String = this.packageName): Long {
    val packageInfo = getPackageInfo(packageName)
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        packageInfo.longVersionCode
    } else {
        packageInfo.versionCode.toLong()
    }
}

fun Context.installApk(apkFile: File, fileProvider: String) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        val contentUri = getUriForFile(this, fileProvider, apkFile)
        intent.setDataAndType(contentUri, "application/vnd.android.package-archive")
    } else {
        intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive")
    }
    startActivity(intent)
}

inline fun <reified T : Activity> Context.startActivity(black: (intent: Intent) -> Unit = {}) {
    val intent = Intent(this, T::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    black(intent)
    startActivity(intent)
}

fun getTopActivity(context: Context): ComponentName? {
    val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    return try {
        am.getRunningTasks(1)[0].topActivity
    } catch (e: Exception) {
        null
    }
}

/**
 * 使用系统root权限静默安装应用
 */
object installer {

    private const val TAG = "ApkTools"

    fun getPackageNameFromApkFile(context: Context, apkFile: File): String? {
        val packageManager = context.packageManager
        return try {
            // 获取APK的PackageInfo
            val packageArchiveInfo = packageManager.getPackageArchiveInfo(
                apkFile.absolutePath,
                PackageManager.GET_META_DATA
            )
            // 设置正确的源Dir以确保正确解析
            packageArchiveInfo?.applicationInfo?.sourceDir = apkFile.absolutePath
            // 返回包名
            packageArchiveInfo?.packageName
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun silenceInstall(apkFile: File, context: Context) {
        val pkgName = getPackageNameFromApkFile(context, apkFile)
        LogUtils.i(TAG, "silenceInstall pkgName:$pkgName")
        val packageInstaller = context.packageManager.packageInstaller
        packageInstaller.registerSessionCallback(object : PackageInstaller.SessionCallback() {
            override fun onCreated(sessionId: Int) {
                LogUtils.i(TAG, "onCreated sessionId:$sessionId")
            }

            override fun onBadgingChanged(sessionId: Int) {
                LogUtils.i(TAG, "onBadgingChanged sessionId:$sessionId")
            }

            override fun onActiveChanged(sessionId: Int, active: Boolean) {
                LogUtils.i(TAG, "onActiveChanged sessionId:$sessionId active:$active")
            }

            override fun onProgressChanged(sessionId: Int, progress: Float) {
                LogUtils.i(TAG, "onProgressChanged sessionId:$sessionId progress:$progress")
            }

            override fun onFinished(sessionId: Int, success: Boolean) {
                LogUtils.i(TAG, "onFinished sessionId:$sessionId success:$success")
            }

        })

        try {
            val sessionParams = PackageInstaller.SessionParams(PackageInstaller.SessionParams.MODE_FULL_INSTALL)
            val sessionId = packageInstaller.createSession(sessionParams)

            packageInstaller.openSession(sessionId).use { session ->
                val inStream = FileInputStream(apkFile)
                val outStream = session.openWrite("COSU", 0, -1)
                inStream.copyTo(outStream)
                outStream.close()
                inStream.close()

                val intent = Intent(context, InstallReceiver::class.java)
                intent.action = "com.example.ACTION_INSTALL_COMPLETE"
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    sessionId,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                session.commit(pendingIntent.intentSender)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun copyData(inputStream: InputStream, outputStream: OutputStream) {
        inputStream.copyTo(outputStream)
    }

    // 定义广播接收器以处理安装结果
    class InstallReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            // 处理安装完成后的逻辑
            LogUtils.i(TAG, "onReceive intent:$intent")
        }
    }
}
