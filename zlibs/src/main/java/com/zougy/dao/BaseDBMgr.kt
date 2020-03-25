package com.zougy.dao

import android.text.TextUtils
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
abstract class BaseDBMgr(dbName: String, dbVersion: Int, path: String? = null) {

    protected val dbMgr: DbManager

    init {
        val config = DbManager.DaoConfig()
        config.dbName = dbName
        config.dbVersion = dbVersion
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

    fun <T> deleteBeanById(clazz: Class<T>, bean: Any) {
        try {
            dbMgr.deleteById(clazz, bean)
        } catch (e: Exception) {
        }
    }

    /**
     * 获取筛选器
     */
    private fun <T> selector(clazz: Class<T>, builder: WhereBuilder? = null): Selector<T> {
        return if (builder == null) {
            dbMgr.selector(clazz)
        } else {
            dbMgr.selector(clazz).where(builder)
        }
    }

    fun <T> getBean(clazz: Class<T>, builder: WhereBuilder? = null): T? {
        return try {
            selector(clazz, builder).findFirst()
        } catch (e: Exception) {
            null
        }
    }

    fun <T> getBeanList(clazz: Class<T>, builder: WhereBuilder? = null): List<T>? {
        return try {
            selector(clazz, builder).findAll()
        } catch (e: Exception) {
            null
        }
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