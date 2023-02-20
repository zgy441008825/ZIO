package com.zougy.zio

import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import com.zougy.commons.ZLog
import com.zougy.netWork.NetWorkTools

class MainActivity : BaseActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        ZLog.d("onCreate", TAG)

        NetWorkTools.registerNetworkState(baseContext, networkCallback)
    }

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            ZLog.i("onAvailable", TAG)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            ZLog.i("onLost", TAG)
        }
    }
}
