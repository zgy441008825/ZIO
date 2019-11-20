package com.zougy.ziolib.files

import org.junit.Test
import java.io.File

/**
 * Description:<br></br>
 * Author:邹高原<br></br>
 * Date:11/20 0020<br></br>
 * Email:441008824@qq.com
 */
class FileFilterHelperTest {

    @Test
    fun getFileType() {
    }

    @Test
    fun getFileFilter() {
    }

    @Test
    fun getFileFilter1() {
    }

    @Test
    fun filter() {
        val filter = FileFilterHelper.filter(FileTypeEnum.FILE_TYPE_UNKNOWN, File("a"), true)
        println("ZLog $filter")
    }
}