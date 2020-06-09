package com.zougy.dao

import org.xutils.DbManager
import org.xutils.db.Selector
import org.xutils.db.sqlite.WhereBuilder
import org.xutils.x
import java.io.File

/**
 * Description:数据库管理基类<br>
 * Author:邹高原<br>
 * Date:03/24 0024<br>
 * Email:441008824@qq.com
 */
abstract class BaseDBMgr() {

    lateinit var dbMgr: DbManager

    fun init(dbName: String, dbVersion: Int = 1, path: String? = null) {
        val config = DbManager.DaoConfig()
        config.dbName = dbName
        config.dbVersion = dbVersion
        config.setDbUpgradeListener { db, oldVersion, newVersion ->
            onDbUpgrade(db, oldVersion, newVersion)
        }
        if (path != null) {
            val file = File(path)
            if (file.exists()) {
                if (file.isDirectory)
                    config.dbDir = file
            } else if (file.mkdirs()) {
                config.dbDir = file
            }
        }
        dbMgr = x.getDb(config)
    }

    fun onDbUpgrade(db: DbManager, oldVersion: Int, newVersion: Int) {

    }

    fun saveBean(bean: Any) {
        dbMgr.save(bean)
    }

    fun saveBindId(bean: Any) {
        dbMgr.saveBindingId(bean)
    }

    fun saveOrUpdate(bean: Any) {
        dbMgr.saveOrUpdate(bean)
    }

    fun deleteBean(bean: Any) {
        try {
            dbMgr.delete(bean)
        } catch (e: Exception) {
        }
    }

    inline fun <reified T> deleteBeanById(bean: T) {
        try {
            dbMgr.deleteById(T::class.java, bean)
        } catch (e: Exception) {
        }
    }

    /**
     * 获取筛选器
     */
    inline fun <reified T> selector(builder: WhereBuilder?): Selector<T>? {
        return try {
            if (builder != null) {
                dbMgr.selector(T::class.java).where(builder)
            } else {
                dbMgr.selector(T::class.java)
            }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 获取一个实例
     */
    inline fun <reified T> getBean(builder: WhereBuilder? = null): T? {
        return selector<T>(builder)?.findFirst()
    }

    /**
     * 获取实例列表
     */
    inline fun <reified T> getList(builder: WhereBuilder? = null): MutableList<T>? {
        return selector<T>(builder)?.findAll()
    }

    /**
     * 分页加载
     */
    inline fun <reified T> getListLimit(builder: WhereBuilder? = null, size: Int, offset: Int): MutableList<T>? {
        return selector<T>(builder)?.limit(size)?.offset(offset)?.findAll()
    }

    /***
     * 获取List并且通过colName列来排序
     */
    inline fun <reified T> getListOrder(builder: WhereBuilder? = null, desc: Boolean = false, colName: String): MutableList<T>? {
        return selector<T>(builder)?.orderBy(colName, desc)?.findAll()
    }

    fun dropDb() {
        try {
            dbMgr.dropDb()
        } catch (e: Exception) {
        }
    }

    fun <T> dropTable(clazz: Class<T>) {
        try {
            dbMgr.dropTable(clazz)
        } catch (e: Exception) {
        }
    }
}