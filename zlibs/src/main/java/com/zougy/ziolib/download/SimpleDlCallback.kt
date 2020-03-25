package com.zougy.ziolib.download

import org.xutils.common.Callback
import java.io.File

/**
 * Description:下载文件回调接口的简单实现，实现了所有方法，但并未有任何方法体。<br>
 * Author:邹高原<br>
 * Date:03/24 0024<br>
 * Email:441008824@qq.com
 */
class SimpleDlCallback : IDownloadCallback {
    override fun onWaiting(downloadBean: DownloadBean) {
    }

    override fun onStarted(downloadBean: DownloadBean) {
    }

    override fun onLoading(total: Long, current: Long, isDownloading: Boolean, downloadBean: DownloadBean) {
    }

    override fun onSuccess(file: File?, downloadBean: DownloadBean) {
    }

    override fun onError(ex: Throwable?, isOnCallback: Boolean, downloadBean: DownloadBean) {
    }

    override fun onCancelled(cex: Callback.CancelledException?, downloadBean: DownloadBean) {
    }

    override fun onFinished(downloadBean: DownloadBean) {
    }
}