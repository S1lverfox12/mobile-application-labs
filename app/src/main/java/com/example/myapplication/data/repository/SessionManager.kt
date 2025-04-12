package com.example.myapplication
import javax.inject.Inject
import javax.inject.Singleton
import dagger.hilt.android.qualifiers.ApplicationContext
import android.content.Context
import kotlinx.coroutines.flow.Flow
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.map
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_session")

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore

    private object PreferencesKeys {
        val USER_ID = intPreferencesKey("user_id")
    }

    suspend fun saveUserId(userId: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_ID] = userId
        }
    }

    suspend fun getUserId(): Flow<Int?> {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.USER_ID]
        }
    }

    suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.USER_ID)
        }
    }
}