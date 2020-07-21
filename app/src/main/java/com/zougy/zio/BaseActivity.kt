package com.zougy.zio

import android.os.Bundle
import com.zougy.ui.activity.ZLifecycleBaseActivity

abstract class BaseActivity : ZLifecycleBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        DensityUnit.setCustomDensity(this, application)
        super.onCreate(savedInstanceState)
    }


}