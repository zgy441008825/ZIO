package com.zougy.ziolib.download

import org.xutils.db.annotation.Column
import org.xutils.db.annotation.Table

/**
 * Description:<br>
 * Author:邹高原<br>
 * Date:03/24 0024<br>
 * Email:441008824@qq.com
 */
@Table(name = ConstantValue.DB_TB_DL_NAME)
class DownloadBean {

    @Column(name = ConstantValue.TB_COL_ID, isId = true)
    var id = 0

    @Column(name = ConstantValue.TB_COL_DL_NAME)
    var fileName = ""

    @Column(name = ConstantValue.TB_COL_DL_PATH)
    var filePath = ""

    @Column(name = ConstantValue.TB_COL_DL_LABEL)
    var label = ""

    @Column(name = ConstantValue.TB_COL_DL_URL)
    var url = ""

    @Column(name = ConstantValue.TB_COL_DL_PROG)
    var progress = 0f

    @Column(name = ConstantValue.TB_COL_DL_FILE_TOTAL)
    var total = 0L

    @Column(name = ConstantValue.TB_COL_DL_CURRENT)
    var current = 0L

    @Column(name = ConstantValue.TB_COL_DL_STATE)
    var state = ConstantValue.DownloadState.DL_STATE_NORMAL

    @Column(name = ConstantValue.TB_COL_DL_MSG)
    var dMessage = ""

    override fun toString(): String {
        return "DownloadBean(id=$id, fileName='$fileName', filePath='$filePath', label='$label', url='$url', progress=$progress, total=$total, current=$current, state=$state, dMessage='$dMessage')"
    }

}