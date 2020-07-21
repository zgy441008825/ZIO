package com.zougy.ui.callback

import androidx.fragment.app.Fragment

interface IFragmentLifecycleCallback : IActivityLifecycleCallback {

    fun onCreateView(fragment: Fragment)

    fun onViewCreated(fragment: Fragment)

}