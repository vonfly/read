package com.vonfly.read.domain.repository

import com.vonfly.read.domain.model.ReaderSettings
import kotlinx.coroutines.flow.Flow

/**
 * 阅读器偏好设置 Repository 接口
 *
 * 负责管理阅读器设置的读写，使用独立的 DataStore 文件 "reader_settings"。
 */
interface ReaderPreferencesRepository {

    /**
     * 观察阅读器设置
     *
     * @return 阅读器设置 Flow
     */
    fun observeSettings(): Flow<ReaderSettings>

    /**
     * 更新字体大小
     *
     * @param fontSize 字体大小（sp）
     */
    suspend fun updateFontSize(fontSize: Float)

    /**
     * 更新行高倍数
     *
     * @param lineHeight 行高倍数
     */
    suspend fun updateLineHeight(lineHeight: Float)

    /**
     * 更新颜色主题
     *
     * @param colorSchemeName 颜色主题名称
     */
    suspend fun updateColorScheme(colorSchemeName: String)
}
