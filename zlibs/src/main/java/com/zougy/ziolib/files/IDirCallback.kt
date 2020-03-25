package com.zougy.ziolib.files

import org.xutils.common.Callback
import java.io.File

/**
 * Description:处理目录时的回调，<br>
 * Author:邹高原<br>
 * Date:11/20 0020<br>
 * Email:441008824@qq.com
 */
interface IDirCallback : Callback.ProgressCallback<File> {

    /**
     * 当文件改变的时候
     */
    fun onFileChange(file: File)
}