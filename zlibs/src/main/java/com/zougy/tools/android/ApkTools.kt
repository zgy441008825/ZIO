package com.zougy.tools.android

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider.getUriForFile
import java.io.File

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
