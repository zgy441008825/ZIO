package com.zougy.zio

import android.app.Application
import com.zougy.ZTools
import com.zougy.log.LogUtils

object App {
    lateinit var instance: Application
}

/**
 * Description:<br>
 * Author:邹高原<br>
 * Date:03/24 0024<br>
 * Email:441008824@qq.com
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        App.instance = this
        ZTools.application = this
        LogUtils.tagPrefix = "ZIO-${BuildConfig.VERSION_NAME}"
    }
}