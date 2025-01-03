package com.zougy.ziolib.files

import android.text.TextUtils
import com.zougy.log.LogUtils
import java.io.Closeable
import java.io.File
import java.io.FileFilter
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Description:定义文件常见操作<br>
 * Author:邹高原<br>
 * Date:11/14 0014<br>
 * Email:441008824@qq.com
 */
object ZFileTools {

    private const val TAG = "ZFileTools"

    fun close(cl: Closeable) {
        try {
            cl.close()
        } catch (e: Exception) {
            LogUtils.e(TAG, "close error:${e.message}")
        }
    }

    fun copy(inputStream: InputStream, outputStream: OutputStream) {
        inputStream.copyTo(outputStream)
    }

    /**
     * 创建文件
     */
    fun createFile(filePath: String): Boolean {
        val file = File(filePath)
        if (file.exists()) return true
        val parentFile = file.parentFile
        if (parentFile != null) {
            if (parentFile.exists()) {
                return file.createNewFile()
            } else if (parentFile.mkdirs()) {
                return file.createNewFile()
            }
        }
        return false
    }

    /**
     * 创建文件
     */
    fun createDir(filePath: String): Boolean {
        val file = File(filePath)
        if (file.exists()) return true
        return file.mkdirs()
    }

    /**
     * 删除文件或者目录
     */
    fun deleteFileOrDir(file: File): Boolean {
        if (!file.exists()) return false
        if (file.isFile) return file.delete()
        val fileList = file.listFiles()
        if (fileList.isNullOrEmpty()) {
            return file.delete()
        } else {
            for (f in fileList) {
                if (f.isFile) {
                    if (!f.delete()) return false
                } else {
                    if (!deleteFileOrDir(f)) return false
                }
            }
        }
        return file.delete()
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

    /**
     * 搜索目录path下的文件
     * @param path 要搜索的目录
     * @param filter:扫描时文件的过滤规则
     * @param child 是否递归进入子目录
     * @param showHidden 是否包含隐藏文件和目录
     * @param excludeNoMedia 是否排除媒体文件目录（目录中包含.nomedia文件） 如果要排除则设置为true
     * @return 返回目录下面的文件和目录，如果child=true则包含子目录中的内容。
     */
    fun searchFile(
        path: String,
        filter: FileFilter? = null,
        child: Boolean = true,
        showHidden: Boolean = false,
        excludeNoMedia: Boolean = false
    ): List<File>? {
        return searchFile(File(path), filter, child, showHidden, excludeNoMedia)
    }

    /**
     * 搜索目录path下的文件
     * @param path 要搜索的目录
     * @param filter:扫描时文件的过滤规则,filter回调是会将扫描到的文件和目录名都进行回调判断
     * @param child 是否递归进入子目录
     * @param showHidden 是否包含隐藏文件和目录
     * @param excludeNoMedia 是否排除媒体文件目录（目录中包含.nomedia文件） 如果要排除则设置为true
     * @return 返回目录下面的文件和目录，如果child=true则包含子目录中的内容。
     */
    fun searchFile(
        path: File,
        filter: FileFilter? = null,
        child: Boolean = true,
        showHidden: Boolean = false,
        excludeNoMedia: Boolean = true
    ): List<File>? {
        if (!path.exists() || path.isFile) {
            return null
        }
        val noMediaFile = File(path, ".nomedia")
        if (excludeNoMedia && noMediaFile.exists()) {
            return null
        }

        val fileList = mutableListOf<File>()
        val fileFilter: FileFilter = filter ?: FileFilter { true }
        val files = path.listFiles(fileFilter) ?: return null
        for (f in files) {
            if (fileFilter.accept(f)) {
                if (f.isFile) {
                    fileList.add(f)
                } else if (f.isDirectory && child) {
                    val fList = searchFile(f, fileFilter, true, showHidden, excludeNoMedia)
                    if (!fList.isNullOrEmpty())
                        fileList.addAll(fList)
                }
            }
        }
        return fileList
    }

    fun readString(filePath: String, charset: String = "UTF-8"): String {
        if (TextUtils.isEmpty(filePath)) return ""
        return readString(File(filePath), charset)
    }

    fun readString(file: File, charset: String = "UTF-8"): String {
        if (!file.exists()) return ""
        val inputStream = FileInputStream(file)
        val fileContent = readString(inputStream, charset)
        close(inputStream)
        return fileContent
    }

    fun readString(inputStream: InputStream?, charset: String = "UTF-8"): String {
        if (inputStream == null) return ""
        val reader = InputStreamReader(inputStream, charset)
        val sb = StringBuilder()
        var len: Int
        val charArray = CharArray(1024 * 4)
        while (true) {
            len = reader.read(charArray)
            if (len == -1) break
            sb.appendRange(charArray, 0, len)
        }
        return sb.toString()
    }

    fun readBytes(inputStream: InputStream?): ByteArray? {
        if (inputStream == null) return null
        val size = inputStream.available()
        val bytes = ByteArray(size)
        inputStream.read(bytes)
        return bytes
    }

    fun write2File(file: File, content: String, charset: String = "UTF-8"): Boolean {
        if (!file.exists()) {
            createFile(file.absolutePath)
        }
        val outputStream = FileOutputStream(file)
        val result = write2File(FileOutputStream(file), content, charset)
        close(outputStream)
        return result
    }

    fun write2File(outputStream: OutputStream?, content: String?, charset: String = "UTF-8"): Boolean {
        if (outputStream == null || TextUtils.isEmpty(content)) return false
        val writer = OutputStreamWriter(outputStream, charset)
        writer.write(content)
        return true
    }

    fun write2File(outputStream: OutputStream?, bytes: ByteArray): Boolean {
        if (outputStream == null) return false
        outputStream.write(bytes)
        outputStream.flush()
        return true
    }

    /**
     * 拷贝文件
     * @param srcFile:源文件
     *
     * @param desFile:目标文件，如果不存在则创建
     *
     * @param override: 如果目标文件存在是否覆盖。true 删除源文件再复制，false 直接跳过。
     */
    fun copyFile(srcFile: File, desFile: File, override: Boolean = true) {
        if (!srcFile.exists()) {
            return
        }
        if (desFile.exists()) {//如果目标文件存在，并且需要覆盖又删除目标文件失败则返回false
            if (override) {
                desFile.delete()
            } else {
                return
            }
        } else if (!createFile(desFile.absolutePath)) {
            return
        }
        try {
            val inputStream = FileInputStream(srcFile)
            val outputStream = FileOutputStream(desFile)
            copy(inputStream, outputStream)
            close(inputStream)
            close(outputStream)
        } catch (e: Exception) {
            LogUtils.e(TAG, "copyFile error:${e.message}")
        }
    }

    /**
     * 拷贝目录
     *
     * @param srcDir 原文件目录
     *
     * @param desDir 目录路径目录
     *
     * @param override 如果目录路径有同名文件是否覆盖
     */
    fun copyDir(srcDir: File, desDir: File, override: Boolean = true) {
        if (srcDir.isFile) {
            copyFile(srcDir, desDir, override)
            return
        }
        if (!desDir.exists()) createDir(desDir.absolutePath)
        val fileList = srcDir.listFiles()
        if (fileList.isNullOrEmpty()) {
            return
        }
        fileList.forEach {
            if (it.isFile) {
                copyFile(it, File(desDir, it.name))
            } else {
                copyDir(it, File(desDir, it.name))
            }
        }
    }
}