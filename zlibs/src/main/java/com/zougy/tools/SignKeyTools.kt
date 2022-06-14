package com.zougy.tools

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * Description:
 * @author GaoYuanZou
 * @date 2022/04/28
 */
class SignKeyTools {

    companion object {
        val instance: SignKeyTools by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            SignKeyTools()
        }

        const val TAG = "SignKeyTools"
    }

    val paramMap = mutableMapOf<String, String>()

    fun addParameter(key: String, value: String) {
        paramMap[key] = value
    }

    fun addParameter(key: String, value: Long) {
        paramMap[key] = value.toString()
    }

    fun removeParameter(key: String, value: String) {
        paramMap.remove(key)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun signKey(secretKey: String): String {
        val sb = StringBuilder()
        paramMap.keys.toSet().toList().sorted().forEach {
            sb.append(it).append("=").append(paramMap[it]).append("&")
        }
        val tmp = sb.toString()
        val url = tmp.substring(0, tmp.length - 1)
        Log.i(TAG, "signKey url: $url")
        val hmac = Mac.getInstance("HmacSHA1")
        hmac.init(SecretKeySpec(secretKey.toByteArray(), "HmacSHA1"))
        return Base64.getEncoder().encodeToString(hmac.doFinal(url.toByteArray()))
    }

}