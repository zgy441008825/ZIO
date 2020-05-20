package com.zougy.ziolib.download

import com.zougy.dao.BaseDBMgr
import org.xutils.db.sqlite.WhereBuilder

/**
 * Description:下载的数据库管理类<br>
 * Author:邹高原<br>
 * Date:03/24 0024<br>
 * Email:441008824@qq.com
 */

class DownloadDBMgr private constructor() : BaseDBMgr() {

    init {
        init(ConstantValue.DB_DL_NAME, ConstantValue.DB_DL_VERSION)
    }

    companion object {
        val instance: DownloadDBMgr by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            DownloadDBMgr()
        }
    }

    fun getDownloadBeanBySavePath(path: String): DownloadBean? {
        val builder = WhereBuilder.b(ConstantValue.TB_COL_DL_PATH, "=", path)
        return getBean(builder)
    }

    fun getDownloadBeanByLabel(label: String): DownloadBean? {
        val builder = WhereBuilder.b(ConstantValue.TB_COL_DL_LABEL, "=", label)
        return getBean(builder)
    }

}