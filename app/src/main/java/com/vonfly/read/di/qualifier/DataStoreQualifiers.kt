package com.vonfly.read.di.qualifier

import javax.inject.Qualifier

/**
 * 用户通用偏好 DataStore 限定符
 *
 * 文件名: "app_settings"
 * 存放用户偏好设置
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AppDataStore
