package com.example.core.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesOf
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalDatabase @Inject constructor(private val database: DataStore<Preferences>) {

    companion object {
        private val LOGINSTATE_KEY = booleanPreferencesKey("loginState")
        private val ACCESSTOKEN_KEY = stringPreferencesKey("accessToken")
        private val USERNAME_KEY = stringPreferencesKey("userName")
    }

    fun getAccessToken(): Flow<String> {
        return database.data.map { preferences ->
            preferences[ACCESSTOKEN_KEY] ?: "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1NmRkZDNiYzhlNDdkMDNjOTRjN2Q2NzgyMTA5NThiZiIsInN1YiI6IjY1MzllOThkOGEwZTliMDBhZDIxMTBhNSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.1XClgLgGzxUHQvXvvlaE9-NCh_5Sn7iQHxLatyVcg1g"
        }
    }

    fun getUserLoginState(): Flow<Boolean> {
        return database.data.map { preferences ->
            preferences[LOGINSTATE_KEY] ?: false
        }
    }

    suspend fun setUserLoginState() {
        database.edit { preferences ->
            preferences[LOGINSTATE_KEY] = true
        }
    }

    fun getUsername(): Flow<String> {
        return database.data.map { preferences ->
            preferences[USERNAME_KEY] ?: ""
        }
    }

    suspend fun setUsername(userName: String) {
        database.edit { preferences ->
            preferences[USERNAME_KEY] = userName
        }
    }

    suspend fun logout() {
        database.edit { preferences ->
            preferences.clear()
        }
    }

}