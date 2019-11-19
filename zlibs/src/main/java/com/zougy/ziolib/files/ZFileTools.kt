package com.zougy.ziolib.files

import android.text.TextUtils
import java.io.*
import java.math.BigInteger
import java.security.MessageDigest

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

    /**
     * 获取文件的MD5
     */
    fun getMD5(file: File): String {
        if (!file.exists()) return ""
        val inputStream = FileInputStream(file)
        val digest = MessageDigest.getInstance("MD5")
        val buffer = ByteArray(1024 * 1024)
        var len: Int
        return try {
            while (true) {
                len = inputStream.read(buffer)
                if (len == -1) break
                digest.update(buffer, 0, len)
            }
            close(inputStream)
            BigInteger(1, digest.digest()).toString(16)
        } catch (e: Exception) {
            close(inputStream)
            ""
        }
    }

    fun readFile2String(filePath: String, charset: String = "UTF-8"): String {
        if (TextUtils.isEmpty(filePath)) return ""
        return readFile2String(File(filePath), charset)
    }

    fun readFile2String(file: File, charset: String = "UTF-8"): String {
        if (!file.exists()) return ""
        val inputStream = FileInputStream(file)
        val fileContent = readFile2String(inputStream, charset)
        close(inputStream)
        return fileContent
    }

    fun readFile2String(inputStream: InputStream?, charset: String = "UTF-8"): String {
        if (inputStream == null) return ""
        val reader = InputStreamReader(inputStream, charset)
        val sb = StringBuilder()
        var len: Int
        val charArray = CharArray(1024)
        while (true) {
            len = reader.read(charArray)
            if (len == -1) break
            sb.append(charArray, 0, len)
        }
        return sb.toString()
    }
}