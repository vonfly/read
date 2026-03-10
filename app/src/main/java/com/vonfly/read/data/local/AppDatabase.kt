package com.vonfly.read.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vonfly.read.data.local.dao.BookDao
import com.vonfly.read.data.local.entity.BookEntity

/**
 * 应用 Room 数据库
 *
 * 数据库版本 1，包含 books 表。
 * Schema 文件导出到 schemas/ 目录，用于版本迁移追踪。
 */
@Database(
    entities = [BookEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    /**
     * 获取书籍 DAO
     */
    abstract fun bookDao(): BookDao
}
