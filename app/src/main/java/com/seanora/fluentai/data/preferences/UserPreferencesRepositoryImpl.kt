package com.seanora.fluentai.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : UserPreferencesRepository {

    private object PreferencesKeys {
        val SELECTED_AVATAR_KEY = stringPreferencesKey("selected_avatar_key")
        val SAVED_READING_IDS = stringSetPreferencesKey("saved_reading_ids")
        val READING_SURFACE_THEME = stringPreferencesKey("reading_surface_theme")
        val READING_FONT_SIZE_SP = floatPreferencesKey("reading_font_size_sp")
    }

    private val safeData = dataStore.data.catch { exception ->
        if (exception is IOException) emit(emptyPreferences()) else throw exception
    }

    override val selectedAvatarKey: Flow<String?> = safeData
        .map { preferences ->
            preferences[PreferencesKeys.SELECTED_AVATAR_KEY]
        }

    override suspend fun getSelectedAvatarKeySync(): String? {
        return selectedAvatarKey.firstOrNull()
    }

    override suspend fun setSelectedAvatarKey(key: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SELECTED_AVATAR_KEY] = key
        }
    }

    override suspend fun clearSelectedAvatarKey() {
        dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.SELECTED_AVATAR_KEY)
        }
    }

    override val savedReadingIds: Flow<Set<String>> = safeData
        .map { preferences -> preferences[PreferencesKeys.SAVED_READING_IDS].orEmpty() }

    override suspend fun toggleSavedReading(articleId: String) {
        dataStore.edit { preferences ->
            val current = preferences[PreferencesKeys.SAVED_READING_IDS].orEmpty()
            preferences[PreferencesKeys.SAVED_READING_IDS] =
                if (articleId in current) current - articleId else current + articleId
        }
    }

    override val readingSurfaceTheme: Flow<String> = safeData
        .map { preferences -> preferences[PreferencesKeys.READING_SURFACE_THEME] ?: "PAPER" }

    override suspend fun setReadingSurfaceTheme(theme: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.READING_SURFACE_THEME] = theme
        }
    }

    override val readingFontSizeSp: Flow<Float> = safeData
        .map { preferences -> (preferences[PreferencesKeys.READING_FONT_SIZE_SP] ?: 20f).coerceIn(16f, 26f) }

    override suspend fun setReadingFontSizeSp(sizeSp: Float) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.READING_FONT_SIZE_SP] = sizeSp.coerceIn(16f, 26f)
        }
    }
}
