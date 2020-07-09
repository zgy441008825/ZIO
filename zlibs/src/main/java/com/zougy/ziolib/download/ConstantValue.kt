package com.zougy.ziolib.download

/**
 * Description:<br>
 * Author:邹高原<br>
 * Date:03/24 0024<br>
 * Email:441008824@qq.com
 */
internal object ConstantValue {

    /**
     * 下载数据库名
     */
    const val DB_DL_NAME = "download.db"

    /**
     * 下载数据库版本
     */
    const val DB_DL_VERSION = 1

    /**
     * 表名——下载
     */
    const val DB_TB_DL_NAME = "download"

    /**
     * 表格列名——id
     */
    const val TB_COL_ID = "id"

    /**
     * 表格列名——文件名
     */
    const val TB_COL_DL_NAME = "fileName"

    /**
     * 表格列名——文件路径
     */
    const val TB_COL_DL_PATH = "filePath"

    /**
     * 表格列名——下载地址
     */
    const val TB_COL_DL_URL = "downloadUrl"

    /**
     * 表格列名——下载标识
     */
    const val TB_COL_DL_LABEL = "label"

    /**
     * 表格列名——下载进度
     */
    const val TB_COL_DL_PROG = "progress"

    /**
     * 表格列名——下载状态
     */
    const val TB_COL_DL_STATE = "state"

    /**
     * 文件总大小
     */
    const val TB_COL_DL_FILE_TOTAL = "total"

    /**
     * 文件当前下载大小
     */
    const val TB_COL_DL_CURRENT = "current"

    /**
     * 表格列名——各状态的相关信息(不一定有值)
     */
    const val TB_COL_DL_MSG = "message"

    /**
     * 下载状态
     */
    object DownloadState {

        /**
         * 无状态，未下载。
         */
        const val DL_STATE_NORMAL = 0

        /**
         * 等待
         */
        const val DL_STATE_WAIT = 1

        /**
         * 下载中
         */
        const val DL_STATE_ING = 2

        /**
         * 下载失败
         */
        const val DL_STATE_FAILED = 3

        /**
         * 下载成功
         */
        const val DL_STATE_SUCCESS = 4
    }
}