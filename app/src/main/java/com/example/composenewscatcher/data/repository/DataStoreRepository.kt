package com.example.composenewscatcher.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.composenewscatcher.util.constant.DataStoreConstant.LANGUAGE_ENGLISH
import com.example.composenewscatcher.util.extension.dataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreRepository @Inject constructor(@ApplicationContext context: Context) {

    private val dataStore: DataStore<Preferences> = context.dataStore

    private object PreferenceKeys {
        val language = stringPreferencesKey("app_language")
    }

    val languagePreferenceFlow: Flow<String> = dataStore.data
        .map {
            it[PreferenceKeys.language] ?: LANGUAGE_ENGLISH
        }

    suspend fun updateLanguage(language: String) {
        dataStore.edit {
            it[PreferenceKeys.language] = language
        }
    }

}