package com.zougy.ziolib.files

import android.text.TextUtils
import org.xutils.common.Callback
import java.io.Closeable
import java.io.File
import java.io.FileFilter
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
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

    fun close(closeable: Closeable?) {
        try {
            closeable?.close()
        } catch (e: Exception) {
        }
    }

    /**
     * 创建文件，可以是目录或者文件
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
     * 创建文件，可以是目录或者文件
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
        val fileList = searchFile(file)
        if (fileList == null || fileList.isEmpty()) return file.delete()
        for (f in fileList) {
            if (f.isFile) {
                if (!f.delete()) return false
            } else {
                if (!deleteFileOrDir(f)) return false
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

    fun searchFile(
        path: String,
        typeEnum: FileTypeEnum? = null,
        filter: FileFilter? = null,
        child: Boolean = true,
        showHidden: Boolean = false,
        containNoMedia: Boolean = false,
        callback: SearchFileCallback? = null
    ): List<File> {
        return searchFile(File(path), typeEnum, filter, child, showHidden, containNoMedia, callback)
    }

    /**
     * 搜索目录path下的文件
     * @param path 要搜索的目录
     * @param typeEnum 文件类型，可为空。为空时返回所有文件和目录
     * @param child 是否递归进入子目录
     * @param showHidden 是否包含隐藏文件和目录
     * @return 返回目录下面的文件和目录，如果child=true则包含子目录中的内容。
     */
    fun searchFile(
        path: File,
        typeEnum: FileTypeEnum? = null,
        filter: FileFilter? = null,
        child: Boolean = true,
        showHidden: Boolean = false,
        containNoMedia: Boolean = false,
        callback: SearchFileCallback? = null
    ): List<File> {
        val fileList = mutableListOf<File>()
        if (!path.exists() || path.isFile) return fileList

        val noMediaFile = path.listFiles { _, name ->
            TextUtils.equals(name.lowercase(), ".nomedia")
        }
        if (!containNoMedia && !noMediaFile.isNullOrEmpty())
            return fileList

        val fileFilter: FileFilter = filter ?: FileFilterHelper.getFileFilter(typeEnum, showHidden)
        val files = path.listFiles() ?: return fileList
        for (f in files) {
            if (f.isFile) {
                if (fileFilter.accept(f)) {
                    fileList.add(f)
                    callback?.onFind(f)
                }
            } else if (f.isDirectory) {
                if (child) {
                    callback?.onInDir(f)
                    val fList = searchFile(f, typeEnum, fileFilter, true, showHidden, containNoMedia, callback)
                    if (fList.isNotEmpty())
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
        val charArray = CharArray(1024)
        while (true) {
            len = reader.read(charArray)
            if (len == -1) break
            sb.append(charArray, 0, len)
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
     * 异步拷贝文件<br>
     *     @param srcFile:源文件
     *     @param desFile:目标文件，如果不存在则创建
     *     @param override: 如果目标文件存在是否覆盖。true 删除源文件再复制，false 直接跳过。
     */
    fun copyFile(srcFile: File, desFile: File, override: Boolean = true, callback: Callback.ProgressCallback<File>?) {
        if (!srcFile.exists()) {
            callback?.onError(FileNotFoundException("$srcFile not found"), false)
            return
        }
        if (desFile.exists()) {//如果目标文件存在，并且需要覆盖又删除目标文件失败则返回false
            if (override) {
                if (!desFile.delete())
                    callback?.onError(IOException("delete $desFile error"), false)
            } else {
                callback?.onSuccess(desFile)
                return
            }
        } else if (!createFile(desFile.absolutePath)) {
            callback?.onError(IOException("create $desFile error"), false)
            return
        }
        Thread {
            kotlin.run {
                try {
                    val inputStream = FileInputStream(srcFile)
                    val outputStream = FileOutputStream(desFile)
                    var readLen: Int
                    val byteArray = ByteArray(1024 * 1024)
                    val totalSize = inputStream.available().toLong()
                    var readCount = 0L
                    callback?.onStarted()
                    callback?.onLoading(totalSize, 0, true)
                    while (true) {
                        readLen = inputStream.read(byteArray)
                        if (readLen == -1) break
                        readCount += readLen
                        callback?.onLoading(totalSize, readCount, true)
                        outputStream.write(byteArray, 0, readLen)
                    }
                    callback?.onSuccess(desFile)
                    outputStream.flush()
                    close(inputStream)
                    close(outputStream)
                } catch (e: Exception) {
                    callback?.onError(e, false)
                } finally {
                    callback?.onFinished()
                }
            }
        }.start()
    }

    fun copyFiles(srcFiles: List<File>, desDir: File, override: Boolean = true, callBack: IDirCallback?) {
        if (srcFiles.isEmpty()) {
            callBack?.onFinished()
            return
        }
        for (f in srcFiles) {
            callBack?.onFileChange(f)
            if (f.isFile) {
                copyFile(f, File(desDir, f.name), override, callBack)
            } else {
                copyDir(f, File(desDir, f.name), override, callBack)
            }
        }
    }

    fun copyDir(srcDir: File, desDir: File, override: Boolean = true, callBack: IDirCallback?) {
        if (srcDir.isFile || desDir.isFile) {
            callBack?.onError(IOException("must be dir"), false)
            return
        }
        if (!desDir.exists()) createDir(desDir.absolutePath)
        val fileList = searchFile(srcDir, null, null, false)
        if (fileList == null || fileList.isEmpty()) {
            callBack?.onFinished()
            return
        }
        copyFiles(fileList, desDir, override, callBack)
    }

    interface SearchFileCallback {

        fun onFind(file: File)

        fun onInDir(dir: File)
    }
}