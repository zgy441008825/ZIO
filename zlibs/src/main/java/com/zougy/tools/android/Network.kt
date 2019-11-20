@file:Suppress("DEPRECATION")

package com.zougy.tools.android

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.text.TextUtils
import org.xutils.common.Callback
import org.xutils.http.RequestParams
import org.xutils.http.request.HttpRequest
import org.xutils.x
import java.io.File
import java.util.*
import kotlin.collections.HashMap

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

    fun getBaseRequestParams(url: String, readTimeOut: Int = 5000, connectTimeOut: Int = 5000, retryCount: Int = 0): RequestParams {
        val request = RequestParams(url)
        request.readTimeout = readTimeOut
        request.connectTimeout = connectTimeOut
        request.maxRetryCount = retryCount
        return request
    }

    fun getParasms(url: String, paramsMap: HashMap<String, Objects>?, headerMap: HashMap<String, String>?): RequestParams {
        val params = getBaseRequestParams(url, 3000, 3000)
        if (paramsMap != null) {
            for ((key, value) in paramsMap) {
                if (!TextUtils.isEmpty(key)) {
                    params.addParameter(key, value)
                }
            }
        }
        if (headerMap != null) {
            for ((key, value) in headerMap) {
                if (!TextUtils.isEmpty(key)) {
                    params.addHeader(key, value)
                }
            }
        }
        return params
    }

    /**
     * 同步的方式获取内容
     */
    inline fun <reified T : Any> getTSync(url: String, paramsMap: HashMap<String, Objects>?, headerMap: HashMap<String, String>?): T? {
        return try {
            x.http().getSync(getParasms(url, paramsMap, headerMap), T::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun <T> get(url: String, params: HashMap<String, Objects>?, headers: HashMap<String, String>?, callback: Callback.CacheCallback<T>) {
        x.http().get(getParasms(url, params, headers), callback)
    }

    fun downloadFile(
        url: String,
        savePath: String,
        params: HashMap<String, Objects>?,
        headers: HashMap<String, String>?,
        callback: Callback.ProgressCallback<File>
    ) {
        val params = getParasms(url, params, headers)
        params.saveFilePath = savePath
        params.isAutoRename = false
        x.http().get(params, callback)
    }
}