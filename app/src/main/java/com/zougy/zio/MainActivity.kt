package com.zougy.zio

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.zougy.log.LogUtils
import com.zougy.zio.dao.AppDbMgr

class MainActivity : FragmentActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppDbMgr.instance.insertUser()
        val user = AppDbMgr.instance.getUser()
        LogUtils.i(TAG, "user:$user")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        LogUtils.i(TAG, "onRequestPermissionsResult requestCode:$requestCode permissions:${permissions.contentToString()} grantResults:${grantResults.contentToString()}")
    }
}
