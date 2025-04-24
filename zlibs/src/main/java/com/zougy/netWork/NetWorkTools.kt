package com.zougy.netWork

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresApi

/**
 * Description:网络帮类<br>
 * Author:邹高原<br>
 * Date:03/24 0024<br>
 * Email:441008824@qq.com
 */

object NetWorkTools {

    /**
     * 当前WiFi是否连接
     */
    fun isWiFiConnected(context: Context): Boolean {
        val cMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cMgr.getNetworkCapabilities(cMgr.activeNetwork)?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: false
        } else {
            cMgr.activeNetworkInfo?.type == ConnectivityManager.TYPE_WIFI
        }
    }

    /**
     * 当前移动网络是否连接
     */
    fun isMobileConnected(context: Context): Boolean {
        val cMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cMgr.getNetworkCapabilities(cMgr.activeNetwork)?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ?: false
        } else {
            cMgr.activeNetworkInfo?.type == ConnectivityManager.TYPE_MOBILE
        }
    }

    /**
     * 判断网络是否可用
     */
    fun isNetworkAvailable(context: Application): Boolean {
        val cMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cMgr.getNetworkCapabilities(cMgr.activeNetwork)?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) ?: false
        } else {
            cMgr.activeNetworkInfo?.let {
                it.isAvailable && it.isConnected
            } ?: false
        }
    }

    /**
     * 注册网络状态回调
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun registerNetworkState(context: Context, networkCallback: ConnectivityManager.NetworkCallback) {
        val cMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        cMgr.requestNetwork(NetworkRequest.Builder().build(), networkCallback)
    }
}