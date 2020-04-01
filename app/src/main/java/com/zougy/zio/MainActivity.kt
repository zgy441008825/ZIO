package com.zougy.zio

import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import com.zougy.commons.ZLog
import com.zougy.ziolib.files.FileTypeEnum
import com.zougy.ziolib.files.ZFileTools
import com.zougy.ziolib.files.ZFileTools.SearchFileCallback
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadingData()
    }

    private fun loadingData() {
        Flowable.just(Environment.getExternalStorageDirectory())
            .subscribeOn(Schedulers.newThread())
            .map { ZFileTools.searchFile(it, FileTypeEnum.FILE_TYPE_MP3, null, true, showHidden = false, callback = searchMp3Callback) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                ZLog.d(it?.size)
            }, {
                it.printStackTrace()
            })
    }

    private val searchMp3Callback = object : SearchFileCallback {
        override fun onFind(file: File) {
        }

        override fun onInDir(dir: File) {
        }
    }
}
