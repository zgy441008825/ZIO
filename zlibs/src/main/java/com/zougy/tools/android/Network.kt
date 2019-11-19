@file:Suppress("DEPRECATION")

package com.zougy.tools.android

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager

/**
 * Description:<br>
 * Author:邹高原<br>
 * Date:11/19 0019<br>
 * Email:441008824@qq.com
 */
object Network {

    /**
     * 检查当前网络是否可用
     */
    @SuppressLint("MissingPermission")
    fun networkIsOS(context: Context): Boolean {
        val connectManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isAvailable && networkInfo.isConnected
    }
}