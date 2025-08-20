package com.example.flightsearchapp.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object PreferencesKeys {
    val SEARCH_QUERY = stringPreferencesKey("search_query")
}

class PreferencesManager(private val dataStore: DataStore<Preferences>) {
    val searchQueryFlow: Flow<String> = dataStore.data
        .map { preferences -> preferences[PreferencesKeys.SEARCH_QUERY] ?: "" }

    suspend fun saveSearchQuery(query: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SEARCH_QUERY] = query
        }
    }
}
