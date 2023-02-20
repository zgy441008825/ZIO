package com.zougy.netWork

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresApi
import org.xutils.common.Callback
import org.xutils.http.RequestParams
import org.xutils.x
import java.io.File

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
     * 当前WiFi是否连接
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

    /**
     * 获取请求体
     * @param url:连接
     * @param cTimeout:连接超时
     * @param rTimeout:读取超时
     * @param retry:失败后重连次数
     */
    private fun getRequestParams(
        url: String,
        cTimeout: Int = 8000,
        rTimeout: Int = 0,
        retry: Int = 0,
        hPara: Map<String, String>? = null,
        para: Map<String, Any>? = null
    ): RequestParams {
        val params = RequestParams(url)
        params.connectTimeout = cTimeout
        params.readTimeout = rTimeout
        params.maxRetryCount = retry
        hPara?.forEach {
            params.addHeader(it.key, it.value)
        }
        para?.forEach {
            params.addParameter(it.key, it.value)
        }
        return params
    }

    /**
     * 获取下载文件的请求体
     */
    private fun getDownloadReq(
        url: String,
        retry: Int = 0,
        savePath: String,
        hPara: Map<String, String>? = null,
        para: Map<String, Any>? = null
    ): RequestParams {
        val params = getRequestParams(url, retry = retry, hPara = hPara, para = para)
        params.saveFilePath = savePath
        params.isAutoRename = false
        params.isAutoResume = true
        return params
    }

    fun <T> get(url: String, callback: Callback.CommonCallback<T>, para: Map<String, Any>? = null) {
        val params = getRequestParams(url, para = para)
        x.http().get(params, callback)
    }

    fun <T> getSync(url: String, clazz: Class<T>, para: Map<String, Any>? = null): T {
        val params = getRequestParams(url, para = para)
        return x.http().getSync(params, clazz)
    }

    fun <T> post(url: String, callback: Callback.CommonCallback<T>, para: Map<String, Any>? = null) {
        val params = getRequestParams(url, para = para)
        x.http().post(params, callback)
    }

    fun <T> postSync(url: String, clazz: Class<T>, para: Map<String, Any>? = null): T {
        val params = getRequestParams(url, para = para)
        return x.http().postSync(params, clazz)
    }

    fun <T> postSync(url: String, clazz: Class<T>, bodyContent: String): T {
        val params = getRequestParams(url)
        params.apply {
            this.bodyContent = bodyContent
            isAsJsonContent = true
        }
        return x.http().postSync(params, clazz)
    }

    fun downLoad(url: String, savePath: String, retry: Int = 0, callback: Callback.ProgressCallback<File>): Callback.Cancelable {
        val para = getDownloadReq(url, retry = retry, savePath = savePath)
        return x.http().get(para, callback)
    }

    fun downLoad(url: String, savePath: String, retry: Int = 0): File {
        val para = getDownloadReq(url, retry = retry, savePath = savePath)
        return x.http().getSync(para, File::class.java)
    }
}