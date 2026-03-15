package com.vonfly.read.data.local.preferences

import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.vonfly.read.di.qualifier.ReaderDataStore
import com.vonfly.read.domain.model.PageTurnMode
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
                brightness = preferences[BRIGHTNESS] ?: DEFAULT_BRIGHTNESS,
                pageTurnMode = PageTurnMode.valueOf(
                    preferences[PAGE_TURN_MODE] ?: DEFAULT_PAGE_TURN_MODE
                ),
                autoPageEnabled = preferences[AUTO_PAGE_ENABLED] ?: DEFAULT_AUTO_PAGE_ENABLED,
                autoPageSpeed = preferences[AUTO_PAGE_SPEED] ?: DEFAULT_AUTO_PAGE_SPEED,
                autoPageInterval = preferences[AUTO_PAGE_INTERVAL] ?: DEFAULT_AUTO_PAGE_INTERVAL
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

    override suspend fun updatePageTurnMode(mode: PageTurnMode) {
        dataStore.edit { preferences ->
            preferences[PAGE_TURN_MODE] = mode.name
        }
    }

    override suspend fun updateAutoPageEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[AUTO_PAGE_ENABLED] = enabled
        }
    }

    override suspend fun updateAutoPageSpeed(speed: Float) {
        dataStore.edit { preferences ->
            preferences[AUTO_PAGE_SPEED] = speed.coerceIn(1.0f, 3.0f)
        }
    }

    override suspend fun updateAutoPageInterval(intervalSeconds: Int) {
        dataStore.edit { preferences ->
            preferences[AUTO_PAGE_INTERVAL] = intervalSeconds.coerceIn(1, 10)
        }
    }

    companion object {
        private const val DEFAULT_FONT_SIZE = 18f
        private const val DEFAULT_LINE_HEIGHT = 1.8f
        private const val DEFAULT_BRIGHTNESS = 1.0f
        private const val DEFAULT_LETTER_SPACING = 0f
        private const val DEFAULT_PAGE_TURN_MODE = "SLIDE"
        private const val DEFAULT_AUTO_PAGE_ENABLED = false
        private const val DEFAULT_AUTO_PAGE_SPEED = 1.0f
        private const val DEFAULT_AUTO_PAGE_INTERVAL = 5

        private val FONT_SIZE = floatPreferencesKey("reader_font_size")
        private val LINE_HEIGHT = floatPreferencesKey("reader_line_height")
        private val COLOR_SCHEME = stringPreferencesKey("reader_color_scheme")
        private val BRIGHTNESS = floatPreferencesKey("reader_brightness")
        private val LETTER_SPACING = floatPreferencesKey("reader_letter_spacing")
        private val PAGE_TURN_MODE = stringPreferencesKey("reader_page_turn_mode")
        private val AUTO_PAGE_ENABLED = booleanPreferencesKey("reader_auto_page_enabled")
        private val AUTO_PAGE_SPEED = floatPreferencesKey("reader_auto_page_speed")
        private val AUTO_PAGE_INTERVAL = intPreferencesKey("reader_auto_page_interval")
    }
}
