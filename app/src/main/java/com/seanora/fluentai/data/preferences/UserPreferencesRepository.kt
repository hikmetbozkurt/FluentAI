package com.seanora.fluentai.data.preferences

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface UserPreferencesRepository {
    val selectedAvatarKey: Flow<String?>
    suspend fun getSelectedAvatarKeySync(): String?
    suspend fun setSelectedAvatarKey(key: String)
    suspend fun clearSelectedAvatarKey()

    val savedReadingIds: Flow<Set<String>> get() = flowOf(emptySet())
    suspend fun toggleSavedReading(articleId: String) {}

    val readingSurfaceTheme: Flow<String> get() = flowOf("PAPER")
    suspend fun setReadingSurfaceTheme(theme: String) {}

    val readingFontSizeSp: Flow<Float> get() = flowOf(20f)
    suspend fun setReadingFontSizeSp(sizeSp: Float) {}
}
