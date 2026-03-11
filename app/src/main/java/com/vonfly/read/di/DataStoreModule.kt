package com.vonfly.read.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.vonfly.read.di.qualifier.AppDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * DataStore 模块
 *
 * 提供 DataStore 实例：
 * - @AppDataStore: 用户偏好（文件名 "app_settings"）
 *
 * ⚠️ 同一进程内每个文件名只能有一个 DataStore 实例，@Singleton 保证这一点
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
}
