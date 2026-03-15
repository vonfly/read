package com.vonfly.read.data.local.preferences

import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.vonfly.read.di.qualifier.ReaderDataStore
import com.vonfly.read.domain.model.ReaderColorScheme
import com.vonfly.read.domain.model.ReaderSettings
import com.vonfly.read.domain.repository.ReaderPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

/**
 * 阅读器偏好设置 Repository 实现
 *
 * 使用 DataStore 持久化阅读器设置到独立文件 "reader_settings"。
 */
class ReaderPreferencesRepositoryImpl @Inject constructor(
    @ReaderDataStore private val dataStore: DataStore<Preferences>
) : ReaderPreferencesRepository {

    override fun observeSettings(): Flow<ReaderSettings> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(androidx.datastore.preferences.core.emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            ReaderSettings(
                fontSize = (preferences[FONT_SIZE] ?: DEFAULT_FONT_SIZE).sp,
                lineHeight = preferences[LINE_HEIGHT] ?: DEFAULT_LINE_HEIGHT,
                letterSpacing = preferences[LETTER_SPACING] ?: DEFAULT_LETTER_SPACING,
                colorScheme = ReaderColorScheme.entries.find {
                    it.name == preferences[COLOR_SCHEME]
                } ?: ReaderColorScheme.Default,
                brightness = preferences[BRIGHTNESS] ?: DEFAULT_BRIGHTNESS
            )
        }

    override suspend fun updateFontSize(fontSize: Float) {
        dataStore.edit { preferences ->
            preferences[FONT_SIZE] = fontSize
        }
    }

    override suspend fun updateLineHeight(lineHeight: Float) {
        dataStore.edit { preferences ->
            preferences[LINE_HEIGHT] = lineHeight
        }
    }

    override suspend fun updateColorScheme(colorSchemeName: String) {
        dataStore.edit { preferences ->
            preferences[COLOR_SCHEME] = colorSchemeName
        }
    }

    override suspend fun updateBrightness(brightness: Float) {
        dataStore.edit { preferences ->
            preferences[BRIGHTNESS] = brightness
        }
    }

    override suspend fun updateLetterSpacing(letterSpacing: Float) {
        dataStore.edit { preferences ->
            preferences[LETTER_SPACING] = letterSpacing
        }
    }

    companion object {
        private const val DEFAULT_FONT_SIZE = 18f
        private const val DEFAULT_LINE_HEIGHT = 1.8f
        private const val DEFAULT_BRIGHTNESS = 1.0f
        private const val DEFAULT_LETTER_SPACING = 0f

        private val FONT_SIZE = floatPreferencesKey("reader_font_size")
        private val LINE_HEIGHT = floatPreferencesKey("reader_line_height")
        private val COLOR_SCHEME = stringPreferencesKey("reader_color_scheme")
        private val BRIGHTNESS = floatPreferencesKey("reader_brightness")
        private val LETTER_SPACING = floatPreferencesKey("reader_letter_spacing")
    }
}
