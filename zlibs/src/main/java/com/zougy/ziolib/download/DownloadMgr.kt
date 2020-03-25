package com.zougy.ziolib.download

import com.zougy.ziolib.download.DownloadMgr.DownloadMgrTaskInfo.executors
import com.zougy.ziolib.download.DownloadMgr.DownloadMgrTaskInfo.taskMap
import java.util.concurrent.Executors

/**
 * Description:下载文件管理类<br>
 * Author:邹高原<br>
 * Date:03/24 0024<br>
 * Email:441008824@qq.com
 */
class DownloadMgr private constructor() {

    object DownloadMgrTaskInfo {

        /**
         * 任务执行器
         */
        val executors = Executors.newFixedThreadPool(3)!!

        /**
         * 用于保存下载的任务，可用于取消下载任务。
         */
        val taskMap = LinkedHashMap<String, DownloadRunnable>()
    }

    companion object {
        val instance: DownloadMgr by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            DownloadMgr()
        }
    }

    /**
     * 增加一个需要下载的任务，如果添加重复任务并且正在下载中则不作任何操作。其他重复任务会删除之前的任务状态重新开始下载。
     */
    fun addDlBean(bean: DownloadBean, callback: IDownloadCallback) {
        val downloadBean = DownloadDBMgr.instance.getDownloadBeanBySavePath(bean.filePath)
        if (downloadBean != null) {
            if (downloadBean.state == ConstantValue.DownloadState.DL_STATE_ING && taskMap.containsKey(bean.label)) {
                return
            }
            DownloadDBMgr.instance.deleteBean(downloadBean)
            if (taskMap.containsKey(bean.label)) taskMap.remove(bean.label)
        }
        bean.state = ConstantValue.DownloadState.DL_STATE_NORMAL
        DownloadDBMgr.instance.saveBean(bean)
        val runnable = DownloadRunnable(bean, callback)
        executors.submit(runnable)
        taskMap[bean.label] = runnable
    }

    /**
     * 取消一个任务的下载
     */
    fun cancelBeanDown(label: String) {
        if (taskMap.containsKey(label)) {
            taskMap[label]?.cancelable?.cancel()
        }
    }

    /**
     * 通过label获取一个下载的事例，包含下载的状态及进度。
     */
    fun getDownloadBean(label: String): DownloadBean? = DownloadDBMgr.instance.getDownloadBeanBySavePath(label)
}