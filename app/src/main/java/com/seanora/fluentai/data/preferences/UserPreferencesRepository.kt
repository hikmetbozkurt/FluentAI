package com.seanora.fluentai.data.preferences

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val selectedAvatarKey: Flow<String?>
    suspend fun getSelectedAvatarKeySync(): String?
    suspend fun setSelectedAvatarKey(key: String)
    suspend fun clearSelectedAvatarKey()
}
