package com.zougy.ziolib.download

import android.util.Log
import com.zougy.netWork.NetWorkTools
import org.xutils.common.Callback
import java.io.File

/**
 * Description:执行下载任务的线程<br>
 * Author:邹高原<br>
 * Date:03/24 0024<br>
 * Email:441008824@qq.com
 */
class DownloadRunnable(private val downloadBean: DownloadBean, private val callback: IDownloadCallback?) : Runnable,
    Callback.ProgressCallback<File> {

    private val downloadDBMgr = DownloadDBMgr.instance

    var cancelable: Callback.Cancelable? = null

    override fun run() {
        cancelable = NetWorkTools.downLoad(downloadBean.url, downloadBean.filePath, callback = this)
    }

    override fun onFinished() {
        callback?.onFinished(downloadBean)
        DownloadMgr.DownloadMgrTaskInfo.taskMap.remove(downloadBean.label)
    }

    override fun onLoading(total: Long, current: Long, isDownloading: Boolean) {
        downloadBean.state = ConstantValue.DownloadState.DL_STATE_ING
        downloadBean.total = total
        downloadBean.current = current
        downloadBean.progress = (current.toFloat() / total.toFloat())
        Log.d("DownloadRunnable", "ZLog onLoading ${downloadBean.progress}")
        downloadDBMgr.saveOrUpdate(downloadBean)
        callback?.onLoading(total, current, isDownloading, downloadBean)
    }

    override fun onStarted() {
        downloadBean.state = ConstantValue.DownloadState.DL_STATE_ING
        downloadDBMgr.saveOrUpdate(downloadBean)
        callback?.onStarted(downloadBean)
    }

    override fun onSuccess(result: File?) {
        downloadBean.state = ConstantValue.DownloadState.DL_STATE_SUCCESS
        downloadDBMgr.saveOrUpdate(downloadBean)
        callback?.onSuccess(result, downloadBean)
    }

    override fun onWaiting() {
        downloadBean.state = ConstantValue.DownloadState.DL_STATE_WAIT
        downloadDBMgr.saveOrUpdate(downloadBean)
        callback?.onWaiting(downloadBean)
    }

    override fun onCancelled(cex: Callback.CancelledException?) {
        downloadBean.state = ConstantValue.DownloadState.DL_STATE_FAILED
        if (cex != null)
            downloadBean.dMessage = cex.message.toString()
        downloadDBMgr.saveOrUpdate(downloadBean)
        callback?.onCancelled(cex, downloadBean)
    }

    override fun onError(ex: Throwable?, isOnCallback: Boolean) {
        downloadBean.state = ConstantValue.DownloadState.DL_STATE_FAILED
        if (ex != null)
            downloadBean.dMessage = ex.message.toString()
        downloadDBMgr.saveOrUpdate(downloadBean)
        callback?.onError(ex, isOnCallback, downloadBean)
    }

}