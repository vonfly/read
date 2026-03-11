package com.vonfly.read.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.vonfly.read.di.qualifier.AppDataStore
import com.vonfly.read.di.qualifier.ReaderDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * DataStore 模块
 *
 * 提供两个独立的 DataStore 实例：
 * - @AppDataStore: 用户通用偏好（文件名 "app_settings"）
 * - @ReaderDataStore: 阅读器设置（文件名 "reader_settings"）
 *
 * ⚠️ 同一进程内每个文件名只能有一个 DataStore 实例，@Singleton 保证这一点
 * ⚠️ 禁止新增第三个实例，新的偏好必须复用以上两个
 */
@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    @AppDataStore
    fun provideAppDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile("app_settings") }
        )

    @Provides
    @Singleton
    @ReaderDataStore
    fun provideReaderDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile("reader_settings") }
        )
}
