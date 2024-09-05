package com.zougy.tools

import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Reader
import java.lang.reflect.Type


/**
 * Description:使用GSON完成json相关转换
 *
 * Author:邹高原
 *
 * Date:11/20 0020
 *
 * Email:441008824@qq.com
 */
object JsonTools {

    /**
     * 将对象转换成Json
     */
    fun toJson(o: Any): String {
        return Gson().toJson(o)
    }

    inline fun <reified T> getBean(reader: Reader, typeToken: Type = object : TypeToken<T>() {}.type): T? {
        return try {
            Gson().fromJson(reader, typeToken)
        } catch (e: Exception) {
            null
        }
    }

    inline fun <reified T> getBean(json: String, typeToken: Type = object : TypeToken<T>() {}.type): T? {
        return try {
            if (TextUtils.isEmpty(json)) null else Gson().fromJson<T>(json, typeToken)
        } catch (e: Exception) {
            null
        }
    }
}