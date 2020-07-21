package com.zougy.ui.callback

import android.app.Activity

interface IActivityLifecycleCallback {

    fun onCreate(activity: Activity)

    fun onStart(activity: Activity)

    fun onResume(activity: Activity)

    fun onRestart(activity: Activity)

    fun onPause(activity: Activity)

    fun onStop(activity: Activity)

    fun onDestroy(activity: Activity)

}