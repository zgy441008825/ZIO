package com.zougy.ziolib.files

import java.io.Serializable

/**
 * Description:定义文件类型<br>
 * Author:邹高原<br>
 * Date:11/18 0018<br>
 * Email:441008824@qq.com
 */
enum class FileTypeEnum : Serializable {
    FILE_TYPE_DIR,
    FILE_TYPE_IMG,
    FILE_TYPE_TEXT,
    FILE_TYPE_VIDEO,
    FILE_TYPE_MP3,
    FILE_TYPE_BIN,
    FILE_TYPE_PDF,
    FILE_TYPE_UNKNOWN
}