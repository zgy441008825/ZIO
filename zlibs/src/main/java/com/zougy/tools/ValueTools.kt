package com.zougy.tools

import java.security.MessageDigest
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * 该文件定义一些基本数据类型的扩展方法
 */

/**
 * String匹配邮件
 */
fun String.matchEmail(): Boolean {
    val regexEmail = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$"
    return this.matches(regexEmail.toRegex())
}

/**
 * String匹配国内座机号码
 */
fun String.matchLandPhoneNumber(): Boolean {
    val regexTel = "^0\\d{2,3}[- ]?\\d{7,8}"
    return this.matches(regexTel.toRegex())
}

/**
 * String匹配国内手机号码
 */
fun String.matchMobilePhoneNumber(): Boolean {
    val regexMobile = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|(147))\\d{8}$"
    return this.matches(regexMobile.toRegex())
}

/**
 * 转换文件大小,根据大小自动添加单位。保留2位小数。
 */
fun Long.formatSize(): String {
    if (this == 0L) {
        return "0B"
    }
    val df = DecimalFormat("#.00")
    return when {
        this < 1024 -> df.format(this.toDouble()) + "B"
        this < 1048576 -> df.format(this.toDouble() / 1024) + "K"
        this < 1073741824 -> df.format(this.toDouble() / 1048576) + "M"
        else -> df.format(this.toDouble() / 1073741824) + "G"
    }
}

/**
 * 转换文件大小,根据大小自动添加单位。保留2位小数。
 */
fun Int.formatSize(): String {
    if (this == 0) {
        return "0B"
    }
    val df = DecimalFormat("#.00")
    return when {
        this < 1024 -> df.format(this.toDouble()) + "B"
        this < 1048576 -> df.format(this.toDouble() / 1024) + "K"
        this < 1073741824 -> df.format(this.toDouble() / 1048576) + "M"
        else -> df.format(this.toDouble() / 1073741824) + "G"
    }
}

/**
 * 输入格式化时间，默认格式"yyyy/MM/dd HH:mm:ss"
 */
fun Date.formatDate(format: String = "yyyy/MM/dd HH:mm:ss"): String {
    return SimpleDateFormat(format, Locale.getDefault()).format(this)
}

/**
 * 输入格式化时间,默认格式"yyyy/MM/dd HH:mm:ss"
 */
fun Long.formatDate(format: String = "yyyy/MM/dd HH:mm:ss"): String {
    return SimpleDateFormat(format, Locale.getDefault()).format(this)
}

/**
 * 格式化小数
 * @param digits 保留多少位小数
 */
fun Double.formatDouble(digits: Int): String {
    val nf = NumberFormat.getInstance()
    nf.maximumFractionDigits = digits
    nf.isGroupingUsed = false
    return nf.format(this)
}

/**
 * 格式化小数
 * @param digits 保留多少位小数
 */
fun Float.formatDouble(digits: Int): String {
    val nf = NumberFormat.getInstance()
    nf.maximumFractionDigits = digits
    nf.isGroupingUsed = false
    return nf.format(this)
}

fun Byte.toHexString(): String = String.format("%02x", this)

/**
 * String字符串获取MD5
 */
fun String.signMD5(): String {
    val digest = MessageDigest.getInstance("MD5")
    digest.update(this.toByteArray())
    val bs = digest.digest()
    val md5 = StringBuilder()
    bs.forEach {
        md5.append(it.toHexString())
    }
    return md5.toString()
}