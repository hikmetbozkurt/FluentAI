package com.seanora.fluentai.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
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
    }

    override val selectedAvatarKey: Flow<String?> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
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
}
