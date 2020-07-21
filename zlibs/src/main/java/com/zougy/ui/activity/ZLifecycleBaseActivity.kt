package com.zougy.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zougy.ui.callback.IActivityLifecycleCallback

abstract class ZLifecycleBaseActivity : AppCompatActivity() {

    var lifecycleCallback: IActivityLifecycleCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleCallback?.onCreate(this)
    }

    override fun onStart() {
        super.onStart()
        lifecycleCallback?.onStart(this)
    }

    override fun onRestart() {
        super.onRestart()
        lifecycleCallback?.onRestart(this)
    }

    override fun onResume() {
        super.onResume()
        lifecycleCallback?.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        lifecycleCallback?.onPause(this)
    }

    override fun onStop() {
        super.onStop()
        lifecycleCallback?.onStop(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleCallback?.onDestroy(this)
    }

}