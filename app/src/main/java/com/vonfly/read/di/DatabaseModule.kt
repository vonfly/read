package com.vonfly.read.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.vonfly.read.data.local.AppDatabase
import com.vonfly.read.data.local.dao.BookDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Room 数据库 Hilt 模块
 *
 * 提供 AppDatabase 和各 DAO 的依赖注入配置。
 * 使用 SingletonComponent 确保数据库实例在整个应用生命周期内唯一。
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * 提供 Room 数据库实例
     *
     * 使用 fallbackToDestructiveMigration() 简化开发期的数据库版本管理。
     * 生产环境应改为 addMigrations() 以保留用户数据。
     *
     * 添加预填充回调，在数据库首次创建时插入测试数据。
     */
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "read.db"
        )
            .fallbackToDestructiveMigration()
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    // 插入测试数据
                    val now = System.currentTimeMillis()
                    db.execSQL(
                        """INSERT INTO books (id, title, coverGradientStart, coverGradientEnd, lastReadAt, readingProgress, currentChapter, createdAt, updatedAt)
                        VALUES
                        ('1', '三体：地球往事三部曲之一', '#667eea', '#764ba2', ${now - 3600000}, 0.45, '第三章 宇宙闪烁', ${now - 86400000}, $now),
                        ('2', '活着：余华长篇小说代表作', '#f093fb', '#f5576c', ${now - 86400000}, 0.72, '第十二章', ${now - 172800000}, ${now - 86400000}),
                        ('3', '围城：钱钟书唯一长篇小说', '#4facfe', '#00f2fe', NULL, 0.0, NULL, ${now - 259200000}, ${now - 172800000}),
                        ('4', '百年孤独：马尔克斯魔幻现实主义巨著', '#fa709a', '#fee140', NULL, 0.0, NULL, ${now - 345600000}, ${now - 259200000}),
                        ('5', '平凡的世界：路遥茅盾文学奖作品', '#a8edea', '#fed6e3', NULL, 0.0, NULL, ${now - 432000000}, ${now - 345600000}),
                        ('6', '超长书名测试：这是一个非常非常非常长的书名用来测试UI显示效果', '#ff9a9e', '#fecfef', NULL, 0.0, NULL, ${now - 500000000}, ${now - 432000000})
                        """.trimIndent()
                    )
                }
            })
            .build()

    /**
     * 提供书籍 DAO
     */
    @Provides
    fun provideBookDao(database: AppDatabase): BookDao =
        database.bookDao()
}
