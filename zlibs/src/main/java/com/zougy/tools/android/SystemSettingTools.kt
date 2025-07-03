package com.zougy.tools.android

import android.database.ContentObserver
import android.provider.Settings
import android.text.TextUtils
import com.zougy.ZTools
import com.zougy.tools.JsonTools

/**
 * @Description: 获取、设置系统设置值相关方法
 *
 * @Author: GaoYuanZou
 *
 * @Email: zougaoy@cqyazaki.com.cn
 *
 * @CreateTime: 2024/05/13 15:14
 */

inline fun <reified T> String.getSettingsSystemValue(default: T?): T? {
    return when (default) {
        is Int -> {
            Settings.System.getInt(ZTools.application.contentResolver, this, default) as T?
        }

        is String -> {
            val s = Settings.System.getString(ZTools.application.contentResolver, this)
            if (TextUtils.isEmpty(s)) {
                default
            } else {
                s as T
            }
        }

        is Long -> {
            Settings.System.getLong(ZTools.application.contentResolver, this, default) as T?
        }

        is Float -> {
            Settings.System.getFloat(ZTools.application.contentResolver, this, default) as T?
        }

        else -> {
            val s = Settings.System.getString(ZTools.application.contentResolver, this)
            if (TextUtils.isEmpty(s)) {
                default
            } else {
                try {
                    JsonTools.getBean<T>(s)
                } catch (e: Exception) {
                    default
                }
            }

        }
    }
}

inline fun <reified T : Any> String.setSettingsSystemValue(value: T) {
    when (value) {
        is Int -> {
            Settings.System.putInt(ZTools.application.contentResolver, this, value)
        }

        is String -> {
            Settings.System.putString(ZTools.application.contentResolver, this, value)
        }

        is Long -> {
            Settings.System.putLong(ZTools.application.contentResolver, this, value)
        }

        is Float -> {
            Settings.System.putFloat(ZTools.application.contentResolver, this, value)
        }

        else -> {
            Settings.System.putString(ZTools.application.contentResolver, this, JsonTools.toJson(value))
        }
    }
}

inline fun <reified T> String.getSettingsGlobalValue(default: T?): T? {
    return when (default) {
        is Int -> {
            Settings.Global.getInt(ZTools.application.contentResolver, this, default) as T?
        }

        is String -> {
            val s = Settings.Global.getString(ZTools.application.contentResolver, this)
            if (TextUtils.isEmpty(s)) {
                default
            } else {
                s as T
            }
        }

        is Long -> {
            Settings.Global.getLong(ZTools.application.contentResolver, this, default) as T?
        }

        is Float -> {
            Settings.Global.getFloat(ZTools.application.contentResolver, this, default) as T?
        }

        else -> {
            val s = Settings.Global.getString(ZTools.application.contentResolver, this)
            if (TextUtils.isEmpty(s)) {
                default
            } else {
                try {
                    JsonTools.getBean<T>(s)
                } catch (e: Exception) {
                    default
                }
            }

        }
    }
}


inline fun <reified T : Any> String.setSettingsGlobalValue(value: T) {
    when (value) {
        is Int -> {
            Settings.Global.putInt(ZTools.application.contentResolver, this, value)
        }

        is String -> {
            Settings.Global.putString(ZTools.application.contentResolver, this, value)
        }

        is Long -> {
            Settings.Global.putLong(ZTools.application.contentResolver, this, value)
        }

        is Float -> {
            Settings.Global.putFloat(ZTools.application.contentResolver, this, value)
        }

        else -> {
            Settings.Global.putString(ZTools.application.contentResolver, this, JsonTools.toJson(value))
        }
    }
}

inline fun <reified T> String.getSettingsSecureValue(default: T?): T? {
    return when (default) {
        is Int -> {
            Settings.Secure.getInt(ZTools.application.contentResolver, this, default) as T?
        }

        is String -> {
            val s = Settings.Secure.getString(ZTools.application.contentResolver, this)
            if (TextUtils.isEmpty(s)) {
                default
            } else {
                s as T
            }
        }

        is Long -> {
            Settings.Secure.getLong(ZTools.application.contentResolver, this, default) as T?
        }

        is Float -> {
            Settings.Secure.getFloat(ZTools.application.contentResolver, this, default) as T?
        }

        else -> {
            val s = Settings.Secure.getString(ZTools.application.contentResolver, this)
            if (TextUtils.isEmpty(s)) {
                default
            } else {
                try {
                    JsonTools.getBean<T>(s)
                } catch (e: Exception) {
                    default
                }
            }

        }
    }
}


inline fun <reified T : Any> String.setSettingsSecureValue(value: T) {
    when (value) {
        is Int -> {
            Settings.Secure.putInt(ZTools.application.contentResolver, this, value)
        }

        is String -> {
            Settings.Secure.putString(ZTools.application.contentResolver, this, value)
        }

        is Long -> {
            Settings.Secure.putLong(ZTools.application.contentResolver, this, value)
        }

        is Float -> {
            Settings.Secure.putFloat(ZTools.application.contentResolver, this, value)
        }

        else -> {
            Settings.Secure.putString(ZTools.application.contentResolver, this, JsonTools.toJson(value))
        }
    }
}

fun String.registerSystemObserver(observer: ContentObserver) {
    ZTools.application.contentResolver.registerContentObserver(Settings.System.getUriFor(this), true, observer)
}

fun String.registerGlobalObserver(observer: ContentObserver) {
    ZTools.application.contentResolver.registerContentObserver(Settings.Global.getUriFor(this), true, observer)
}

fun String.registerSecureObserver(observer: ContentObserver) {
    ZTools.application.contentResolver.registerContentObserver(Settings.Secure.getUriFor(this), true, observer)
}