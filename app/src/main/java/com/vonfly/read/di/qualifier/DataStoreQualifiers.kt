package com.vonfly.read.di.qualifier

import javax.inject.Qualifier

/**
 * 用户通用偏好 DataStore 限定符
 *
 * 文件名: "app_settings"
 * 存放与阅读器无关的用户偏好：界面语言、通知开关等
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AppDataStore

/**
 * 阅读器偏好 DataStore 限定符
 *
 * 文件名: "reader_settings"
 * 存放阅读器专属设置：字体大小、行距、背景色等
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ReaderDataStore
