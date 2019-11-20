package com.zougy.tools

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.text.TextUtils
import java.io.Reader
import java.lang.reflect.Type


/**
 * Description:使用GSON完成json相关转换<br>
 * Author:邹高原<br>
 * Date:11/20 0020<br>
 * Email:441008824@qq.com
 */
object JsonTools {

    /**
     * 将对象转换成Json
     */
    fun toJson(o: Any): String {
        return Gson().toJson(o)
    }

    /**
     * 解析json到T
     */
    fun <T> getBean(json: Reader?, tClass: Class<T>): T? {
        return if (json == null) null else Gson().fromJson(json, TypeToken.get(tClass).type)
    }

    fun <T> getBean(json: String, tClass: Class<T>): T? {
        return if (TextUtils.isEmpty(json)) null else Gson().fromJson<T>(json, TypeToken.get(tClass).type)
    }

    fun <T> getBean(reader: Reader?): T? {
        return if (reader == null) null else Gson().fromJson(reader, object : TypeToken<T>() {

        }.type)
    }

    fun <T> getBean(reader: String?): T? {
        return if (reader == null) null else Gson().fromJson<T>(reader, object : TypeToken<T>() {

        }.type)
    }

    fun <T> getBean(reader: Reader, typeToken: Type): T {
        return Gson().fromJson(reader, typeToken)
    }

    fun <T> getBean(reader: String, typeToken: Type): T {
        return Gson().fromJson(reader, typeToken)
    }
}