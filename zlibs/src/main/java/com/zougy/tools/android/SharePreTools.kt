package com.zougy.tools.android

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import org.xutils.x

/**
 * Description:<br>
 * Author:邹高原<br>
 * Date:11/20 0020<br>
 * Email:441008824@qq.com
 */
class SharePreTools private constructor() {

    val sharedPreferences: SharedPreferences = x.app().getSharedPreferences("ZSharePreferences", Context.MODE_PRIVATE)

    companion object {
        val instance = ShareHolder.holder
    }

    private object ShareHolder {
        val holder = SharePreTools()
    }

    inline fun <reified T> put(value: T, key: String) {
        sharedPreferences.edit {
            when (value) {
                is String -> putString(key, value)
                is Int -> putInt(key, value)
                is Boolean -> putBoolean(key, value)
                is Float -> putFloat(key, value)
                is Long -> putLong(key, value)
            }
        }
    }

    fun remove(key: String) {
        sharedPreferences.edit { remove(key) }
    }

    inline fun <reified T : Any> get(key: String, default: T): T? {
        sharedPreferences.apply {
            return when (default) {
                is String -> getString(key, default) as T?
                is Int -> getInt(key, default) as T?
                is Boolean -> getBoolean(key, default) as T?
                is Float -> getFloat(key, default) as T?
                else -> getLong(key, default as Long) as T?
            }
        }
    }

    fun getString(key: String, default: String? = null): String? {
        return sharedPreferences.getString(key, default)
    }

    fun getInt(key: String, default: Int = 0): Int {
        return sharedPreferences.getInt(key, default)
    }

    fun getFloat(key: String, default: Float = 0.0f): Float {
        return sharedPreferences.getFloat(key, default)
    }

    fun getLong(key: String, default: Long = 0): Long {
        return sharedPreferences.getLong(key, default)
    }

    fun getBoolean(key: String, default: Boolean = false): Boolean {
        return sharedPreferences.getBoolean(key, default)
    }

}