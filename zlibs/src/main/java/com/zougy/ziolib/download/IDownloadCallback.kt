package com.zougy.ziolib.download

import org.xutils.common.Callback.CancelledException
import java.io.File

/**
 * Description: 下载文件的回调接口<br>
 * Author:邹高原<br>
 * Date:03/24 0024<br>
 * Email:441008824@qq.com
 */
interface IDownloadCallback {

    fun onWaiting(downloadBean: DownloadBean)

    fun onStarted(downloadBean: DownloadBean)

    fun onLoading(total: Long, current: Long, isDownloading: Boolean, downloadBean: DownloadBean)

    fun onSuccess(file: File?, downloadBean: DownloadBean)

    fun onError(ex: Throwable?, isOnCallback: Boolean, downloadBean: DownloadBean)

    fun onCancelled(cex: CancelledException?, downloadBean: DownloadBean)

    fun onFinished(downloadBean: DownloadBean)

}