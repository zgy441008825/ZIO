package com.zougy.tools

import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Description:<br>
 * Author:邹高原<br>
 * Date:11/19 0019<br>
 * Email:441008824@qq.com
 */
object StringTools {

    /**
     * 匹配邮件
     */
    fun matchEmail(email: String?): Boolean {
        val REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$"
        return email != null && email.matches(Regex(REGEX_EMAIL))
    }

    /**
     * 匹配国内座机号码
     */
    fun matchLandPhoneNumber(phone: String?): Boolean {
        val REGEX_TEL = "^0\\d{2,3}[- ]?\\d{7,8}"
        return phone != null && phone.matches(Regex(REGEX_TEL))
    }

    /**
     * 转换文件大小,根据大小自动添加单位。保留2位小数。
     */
    fun formatSize(fileS: Long): String {
        if (fileS == 0L) {
            return "0B"
        }
        val df = DecimalFormat("#.00")
        return when {
            fileS < 1024 -> df.format(fileS.toDouble()) + "B"
            fileS < 1048576 -> df.format(fileS.toDouble() / 1024) + "K"
            fileS < 1073741824 -> df.format(fileS.toDouble() / 1048576) + "M"
            else -> df.format(fileS.toDouble() / 1073741824) + "G"
        }
    }

    /**
     * 格式化小数
     * @param digits 保留多少位小数
     */
    fun formatDouble(value: Double, digits: Int): String {
        val nf = NumberFormat.getInstance()
        nf.maximumFractionDigits = digits
        nf.isGroupingUsed = false
        return nf.format(value)
    }


    /**
     * 输入格式化时间
     */
    fun formatDate(format: String, date: Date = Date()): String {
        return SimpleDateFormat(format, Locale.getDefault()).format(date)
    }

    /**
     * 输入格式化时间
     */
    fun formatDate(format: String, date: Long = System.currentTimeMillis()): String {
        return SimpleDateFormat(format, Locale.getDefault()).format(date)
    }
}