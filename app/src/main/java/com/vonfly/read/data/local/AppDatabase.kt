package com.vonfly.read.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vonfly.read.data.local.dao.BookDao
import com.vonfly.read.data.local.dao.BookDetailDao
import com.vonfly.read.data.local.dao.ChapterDao
import com.vonfly.read.data.local.entity.BookDetailEntity
import com.vonfly.read.data.local.entity.BookEntity
import com.vonfly.read.data.local.entity.ChapterEntity

/**
 * 应用 Room 数据库
 *
 * 数据库版本 2，包含 books、book_details、chapters 表。
 * Schema 文件导出到 schemas/ 目录，用于版本迁移追踪。
 */
@Database(
    entities = [
        BookEntity::class,
        BookDetailEntity::class,
        ChapterEntity::class
    ],
    version = 2,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    /**
     * 获取书籍 DAO
     */
    abstract fun bookDao(): BookDao

    /**
     * 获取书籍详情 DAO
     */
    abstract fun bookDetailDao(): BookDetailDao

    /**
     * 获取章节 DAO
     */
    abstract fun chapterDao(): ChapterDao
}
