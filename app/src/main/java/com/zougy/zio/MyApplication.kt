package com.zougy.zio

import android.app.Application
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
        x.Ext.init(this)
        x.Ext.setDebug(true)
    }
}