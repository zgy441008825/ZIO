package com.zougy.ziolib.files

import android.annotation.SuppressLint
import android.text.TextUtils
import java.io.File
import java.io.FileFilter
import java.util.*

/**
 * Description:<br>
 * Author:邹高原<br>
 * Date:11/18 0018<br>
 * Email:441008824@qq.com
 */
object FileFilterHelper {
    private val fileTypeMap = mapOf(
        FileTypeEnum.FILE_TYPE_IMG to arrayOf("jpg", "jpeg", "gif", "png", "bmp"),
        FileTypeEnum.FILE_TYPE_TEXT to arrayOf("txt", "log"),
        FileTypeEnum.FILE_TYPE_VIDEO to arrayOf(
            "rm",
            "rmvb",
            "mp4",
            "3gp",
            "avi",
            "wmv",
            "mtv",
            "flv",
            "dat",
            "mkv",
            "f4v",
            "mov",
            "mpg",
            "ts",
            "asf"
        ),
        FileTypeEnum.FILE_TYPE_BIN to arrayOf("bin"),
        FileTypeEnum.FILE_TYPE_PDF to arrayOf("pdf"),
        FileTypeEnum.FILE_TYPE_MP3 to arrayOf("flac", "ape", "wav", "mp3", "aac")//alac
    )

    private fun getSuffix(fileTypeEnum: FileTypeEnum): Array<String>? {
        return fileTypeMap[fileTypeEnum]
    }

    @SuppressLint("DefaultLocale")
    fun getFileType(suffix: String): FileTypeEnum {
        for ((key, value) in fileTypeMap) {
            if (suffix in value || suffix.lowercase(Locale.getDefault()) in value) {
                return key
            }
        }
        return FileTypeEnum.FILE_TYPE_UNKNOWN
    }

    fun getFileFilter(suffix: String, showHidden: Boolean): FileFilter {
        if (TextUtils.isEmpty(suffix)) return FileFilter { showHidden || !it.isHidden }
        return FileFilter {
            it.name.endsWith(suffix) && (showHidden || !it.isHidden)
        }
    }

    fun getFileFilter(typeEnum: FileTypeEnum?, showHidden: Boolean): FileFilter {
        if (typeEnum == null || typeEnum == FileTypeEnum.FILE_TYPE_UNKNOWN) {
            return FileFilter { showHidden || !it.isHidden }
        }
        if (typeEnum == FileTypeEnum.FILE_TYPE_DIR) {
            return FileFilter { it.isDirectory && (showHidden || !it.isHidden) }
        }
        return FileFilter { filter(typeEnum, it, showHidden) }
    }

    fun filter(fileTypeEnum: FileTypeEnum, fileName: File, showHidden: Boolean): Boolean {
        if (!showHidden && fileName.isHidden) return false
        if (fileName.isDirectory) return fileTypeEnum == FileTypeEnum.FILE_TYPE_DIR
        val suffix = getSuffix(fileTypeEnum) ?: return false
        for (s in suffix) {
            if (fileName.name.endsWith(".$s")) return true
        }
        return false
    }

}