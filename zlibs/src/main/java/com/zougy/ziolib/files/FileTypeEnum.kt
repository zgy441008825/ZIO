package com.zougy.ziolib.files

import java.io.Serializable

/**
 * Description:定义文件类型<br>
 * Author:邹高原<br>
 * Date:11/18 0018<br>
 * Email:441008824@qq.com
 */
enum class FileTypeEnum : Serializable {
    /**
     * 目录
     */
    FILE_TYPE_DIR,

    /**
     * 图片文件，"jpg", "jpeg", "gif", "png", "bmp"
     */
    FILE_TYPE_IMG,

    /**
     * "txt", "log"
     */
    FILE_TYPE_TEXT,

    /**
     * "rm", "rmvb", "mp4", "3gp", "avi", "wmv", "mtv", "flv", "dat"
     */
    FILE_TYPE_VIDEO,

    /**
     * "flac", "ape", "wav", "mp3", "aac"
     */
    FILE_TYPE_MP3,

    /**
     * "bin"
     */
    FILE_TYPE_BIN,

    /**
     * "pdf"
     */
    FILE_TYPE_PDF,

    /**
     * 未知文件
     */
    FILE_TYPE_UNKNOWN
}