package com.zougy.ziolib.files

import android.text.TextUtils
import java.io.Closeable
import java.io.File
import java.io.InputStream

/**
 * Description:定义文件常见操作<br>
 * Author:邹高原<br>
 * Date:11/14 0014<br>
 * Email:441008824@qq.com
 */
object ZFileTools {

    fun close(closeable: Closeable) {
        try {
            closeable.close()
        } catch (e: Exception) {
        }
    }

    /**
     * 创建文件，可以是目录或者文件
     */
    fun createFileOrDir(filePath: String): Boolean {
        if (TextUtils.isEmpty(filePath))
            return false
        val file = File(filePath)
        if (file.exists()) return true
        if (file.isFile) {
            val parentFile = file.parentFile
            if (parentFile != null) {
                if (parentFile.exists()) {
                    return file.createNewFile()
                } else if (parentFile.mkdirs()) {
                    return file.createNewFile()
                }
            }
        } else {
            return file.mkdirs()
        }
        return false
    }
}