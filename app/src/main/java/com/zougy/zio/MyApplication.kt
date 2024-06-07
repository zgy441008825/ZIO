package com.zougy.zio

import android.app.Application
import com.zougy.log.LogUtils
import com.zougy.tools.DefaultApkCrashHandler
import org.xutils.x

/**
 * Description:<br>
 * Author:邹高原<br>
 * Date:03/24 0024<br>
 * Email:441008824@qq.com
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        LogUtils.tagPrefix = "ZIO-${BuildConfig.VERSION_NAME}"
        x.Ext.init(this)
    }
}