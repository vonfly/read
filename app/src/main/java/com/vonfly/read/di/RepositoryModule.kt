package com.vonfly.read.di

import com.vonfly.read.data.repository.BookRepositoryImpl
import com.vonfly.read.data.repository.StoreRepositoryImpl
import com.vonfly.read.domain.repository.BookRepository
import com.vonfly.read.domain.repository.StoreRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Repository Hilt 模块
 *
 * 负责将 Repository 接口绑定到对应的实现类。
 * 使用 abstract class + @Binds 比 @Provides 更高效（生成的代码更少）。
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    /**
     * 绑定书籍仓库接口到实现类
     */
    @Binds
    @Singleton
    abstract fun bindBookRepository(
        impl: BookRepositoryImpl
    ): BookRepository

    /**
     * 绑定书城仓库接口到实现类
     */
    @Binds
    @Singleton
    abstract fun bindStoreRepository(
        impl: StoreRepositoryImpl
    ): StoreRepository
}
