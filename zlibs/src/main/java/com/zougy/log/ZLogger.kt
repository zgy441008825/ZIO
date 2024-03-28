package com.zougy.log

import android.text.TextUtils
import android.util.Log

/**
 * @Description:
 * 内部默认实现的logger
 * @CreateTime:2023/7/25 23:21
 * @Author:ZouGaoYuan
 * @Email:441008825@qq.com
 */
class ZLogger : ILogger {

    companion object {
        private const val TAG = "ZLogger"
    }

    override fun d(tag: String?, msg: String) {
        Log.d(if (TextUtils.isEmpty(tag)) TAG else tag, msg)
    }

    override fun i(tag: String?, msg: String) {
        Log.i(if (TextUtils.isEmpty(tag)) TAG else tag, msg)
    }

    override fun w(tag: String?, msg: String, e: Throwable?) {
        e?.apply {
            Log.w(TAG, msg, e)
        } ?: {
            Log.w(TAG, msg)
        }
    }

    override fun e(tag: String?, msg: String, e: Throwable?) {
        e?.apply {
            Log.e(TAG, msg, e)
        } ?: {
            Log.e(TAG, msg)
        }
    }
}