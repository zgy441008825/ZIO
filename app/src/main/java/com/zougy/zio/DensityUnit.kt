package com.zougy.zio

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks
import android.content.res.Configuration

object DensityUnit {

    private var sNoncompatDensity = 0f
    private var sNoncompatScaledDensity = 0f

    fun setCustomDensity(activity: Activity, application: Application) {
        val dm = application.resources.displayMetrics
        if (sNoncompatDensity == 0f) {
            sNoncompatDensity = dm.density
            sNoncompatScaledDensity = dm.scaledDensity
            application.registerComponentCallbacks(object : ComponentCallbacks {
                override fun onLowMemory() {
                }

                override fun onConfigurationChanged(newConfig: Configuration) {
                    if (newConfig.fontScale > 0) {
                        sNoncompatScaledDensity = application.resources.displayMetrics.scaledDensity
                    }
                }

            })
        }
        val targetDensity = dm.widthPixels / 360f
        val targetScaledDensity = targetDensity * (sNoncompatScaledDensity / sNoncompatDensity)
        val targetDensityDpi = (160 * targetDensity).toInt()

        dm.let {
            it.density = targetDensity
            it.scaledDensity = targetScaledDensity
            it.densityDpi = targetDensityDpi
        }

        activity.resources.displayMetrics.let {
            it.density = targetDensity
            it.scaledDensity = targetScaledDensity
            it.densityDpi = targetDensityDpi
        }
    }

}