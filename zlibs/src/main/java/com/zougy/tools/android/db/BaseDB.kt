package com.zougy.tools.android.db

import org.xutils.DbManager
import org.xutils.db.Selector
import org.xutils.db.sqlite.WhereBuilder

/**
 * Description:基于xUtils数据库的基类<br>
 *     定义几个常用方法
 * Author:邹高原<br>
 * Date:11/20 0020<br>
 * Email:441008824@qq.com
 */
abstract class BaseDB {
    lateinit var dbManager: DbManager

    inline fun <reified T> getSelector(builder: WhereBuilder?): Selector<T> {
        return if (builder != null) {
            dbManager.selector(T::class.java).where(builder)
        } else {
            dbManager.selector(T::class.java)
        }
    }

    /**
     * 分页加载
     */
    inline fun <reified T> getListLimit(builder: WhereBuilder?, size: Int, offset: Int): MutableList<T> {
        return getSelector<T>(builder).limit(size).offset(offset).findAll()
    }

    inline fun <reified T> getList(builder: WhereBuilder?): MutableList<T> {
        return getSelector<T>(builder).findAll()
    }

    /***
     * 获取List并且通过colName列来排序
     */
    inline fun <reified T> getListOrder(builder: WhereBuilder?, desc: Boolean = false, colName: String): MutableList<T> {
        return getSelector<T>(builder).orderBy(colName, desc).findAll()
    }

    /**
     * 获取一个实例
     */
    inline fun <reified T> getBean(builder: WhereBuilder?): T {
        return getSelector<T>(builder).findFirst()
    }

}