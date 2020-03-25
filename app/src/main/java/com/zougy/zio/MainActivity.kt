package com.zougy.zio

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.reflect.TypeToken
import com.zougy.tools.JsonTools
import com.zougy.views.onClickOnShake
import com.zougy.ziolib.download.DownloadBean
import com.zougy.ziolib.download.DownloadMgr
import com.zougy.ziolib.download.IDownloadCallback
import com.zougy.ziolib.files.ZFileTools
import kotlinx.android.synthetic.main.activity_main.*
import org.xutils.common.Callback
import java.io.File
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private var beanList = mutableListOf<TideDemoBean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadingData()
        requestPermission()
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), 0x1001)
        }
    }

    private fun loadingData() {
        Thread(Runnable {
            val beanS: MutableList<TideDemoBean>? = JsonTools.getBean(
                InputStreamReader(resources.assets.open("member_landing_data.json"))
            )
            if (beanS == null || beanS.isEmpty()) return@Runnable
            beanList.clear()
            beanList.addAll(beanS)
            runOnUiThread {
                val adapter = MainViewAdapter(beanList!!)
                mainRecyclerView.adapter = adapter
                mainRecyclerView.layoutManager = GridLayoutManager(this, 2)
                adapter.setOnItemChildClickListener { adapter, view, position ->
                    val downloadBean = DownloadBean()
                    downloadBean.fileName = beanList[position].name.zhHans + ".mp3"
                    downloadBean.filePath = "$externalCacheDir/demoSound/${downloadBean.fileName}"
                    downloadBean.url = beanList[position].demo_sound_url
                    downloadBean.label = beanList[position].demo_sound_url
                    Log.d("MainActivity", "ZLog loadingData $downloadBean")
                    DownloadMgr.instance.addDlBean(downloadBean, dlCallback)
                }
            }
        }).start()
        mainBtDownloadAll.onClickOnShake({
            for (bean in beanList) {
                val downloadBean = DownloadBean()
                downloadBean.fileName = bean.name.zhHans + ".mp3"
                downloadBean.filePath = "${Environment.getExternalStorageDirectory()}/tide/demoSound/${downloadBean.fileName}"
                downloadBean.url = bean.demo_sound_url
                downloadBean.label = bean.demo_sound_url
                Log.d("MainActivity", "ZLog loadingData $downloadBean")
                DownloadMgr.instance.addDlBean(downloadBean, dlCallback)
            }
        })
    }

    object dlCallback : IDownloadCallback {
        override fun onFinished(bean: DownloadBean) {
            Log.d("callback", "ZLog onFinished ")
        }

        override fun onLoading(total: Long, current: Long, isDownloading: Boolean, bean: DownloadBean) {
            Log.d("callback", "ZLog onLoading $current/$total")
        }

        override fun onStarted(bean: DownloadBean) {
            Log.d("callback", "ZLog onStarted ")
        }

        override fun onSuccess(result: File?, bean: DownloadBean) {
            Log.d("callback", "ZLog onSuccess $result")
        }

        override fun onWaiting(bean: DownloadBean) {
            Log.d("callback", "ZLog onWaiting ")
        }

        override fun onCancelled(cex: Callback.CancelledException?, bean: DownloadBean) {
            Log.d("callback", "ZLog onCancelled $cex")
        }

        override fun onError(ex: Throwable?, isOnCallback: Boolean, bean: DownloadBean) {
            Log.d("callback", "ZLog onError $ex")
        }

    }
}
