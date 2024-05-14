package com.zougy.tools.android

import android.database.ContentObserver
import android.provider.Settings
import android.text.TextUtils
import com.zougy.tools.JsonTools
import org.xutils.x

/**
 * @Description:
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
            Settings.System.getInt(x.app().contentResolver, this, default) as T?
        }

        is String -> {
            val s = Settings.System.getString(x.app().contentResolver, this)
            if (TextUtils.isEmpty(s)) {
                default
            } else {
                s as T
            }
        }

        is Long -> {
            Settings.System.getLong(x.app().contentResolver, this, default) as T?
        }

        is Float -> {
            Settings.System.getFloat(x.app().contentResolver, this, default) as T?
        }

        else -> {
            val s = Settings.System.getString(x.app().contentResolver, this)
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
            Settings.System.putInt(x.app().contentResolver, this, value)
        }

        is String -> {
            Settings.System.putString(x.app().contentResolver, this, value)
        }

        is Long -> {
            Settings.System.putLong(x.app().contentResolver, this, value)
        }

        is Float -> {
            Settings.System.putFloat(x.app().contentResolver, this, value)
        }

        else -> {
            Settings.System.putString(x.app().contentResolver, this, JsonTools.toJson(value))
        }
    }
}

inline fun <reified T> String.getSettingsGlobalValue(default: T?): T? {
    return when (default) {
        is Int -> {
            Settings.Global.getInt(x.app().contentResolver, this, default) as T?
        }

        is String -> {
            val s = Settings.Global.getString(x.app().contentResolver, this)
            if (TextUtils.isEmpty(s)) {
                default
            } else {
                s as T
            }
        }

        is Long -> {
            Settings.Global.getLong(x.app().contentResolver, this, default) as T?
        }

        is Float -> {
            Settings.Global.getFloat(x.app().contentResolver, this, default) as T?
        }

        else -> {
            val s = Settings.Global.getString(x.app().contentResolver, this)
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
            Settings.Global.putInt(x.app().contentResolver, this, value)
        }

        is String -> {
            Settings.Global.putString(x.app().contentResolver, this, value)
        }

        is Long -> {
            Settings.Global.putLong(x.app().contentResolver, this, value)
        }

        is Float -> {
            Settings.Global.putFloat(x.app().contentResolver, this, value)
        }

        else -> {
            Settings.Global.putString(x.app().contentResolver, this, JsonTools.toJson(value))
        }
    }
}

inline fun <reified T> String.getSettingsSecureValue(default: T?): T? {
    return when (default) {
        is Int -> {
            Settings.Secure.getInt(x.app().contentResolver, this, default) as T?
        }

        is String -> {
            val s = Settings.Secure.getString(x.app().contentResolver, this)
            if (TextUtils.isEmpty(s)) {
                default
            } else {
                s as T
            }
        }

        is Long -> {
            Settings.Secure.getLong(x.app().contentResolver, this, default) as T?
        }

        is Float -> {
            Settings.Secure.getFloat(x.app().contentResolver, this, default) as T?
        }

        else -> {
            val s = Settings.Secure.getString(x.app().contentResolver, this)
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
            Settings.Secure.putInt(x.app().contentResolver, this, value)
        }

        is String -> {
            Settings.Secure.putString(x.app().contentResolver, this, value)
        }

        is Long -> {
            Settings.Secure.putLong(x.app().contentResolver, this, value)
        }

        is Float -> {
            Settings.Secure.putFloat(x.app().contentResolver, this, value)
        }

        else -> {
            Settings.Secure.putString(x.app().contentResolver, this, JsonTools.toJson(value))
        }
    }
}

fun String.registerSystemObserver(observer: ContentObserver) {
    x.app().contentResolver.registerContentObserver(Settings.System.getUriFor(this), true, observer)
}

fun String.registerGlobalObserver(observer: ContentObserver) {
    x.app().contentResolver.registerContentObserver(Settings.Global.getUriFor(this), true, observer)
}

fun String.registerSecureObserver(observer: ContentObserver) {
    x.app().contentResolver.registerContentObserver(Settings.Secure.getUriFor(this), true, observer)
}