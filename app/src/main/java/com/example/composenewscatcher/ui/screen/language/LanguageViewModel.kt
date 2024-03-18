package com.example.composenewscatcher.ui.screen.language

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composenewscatcher.data.model.Article
import com.example.composenewscatcher.data.repository.DataStoreRepository
import com.example.composenewscatcher.data.repository.NewsCatcherRepository
import com.example.composenewscatcher.util.extension.excludeSavedArticles
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val newsCatcherRepository: NewsCatcherRepository
): ViewModel() {

    private var cachedArticles = newsCatcherRepository.LocalDataSource().newsCatcher.getAllCachedArticles()

    fun onLanguageScreenEvent (languageScreenEvent: LanguageScreenEvent) = when (languageScreenEvent) {
        is LanguageScreenEvent.UpdateAppLanguage -> {
            updateAppLanguage(languageScreenEvent.language, languageScreenEvent.context)
        }
    }

    private fun deleteAll(vararg article: Article) = viewModelScope.launch {
        newsCatcherRepository.LocalDataSource().newsCatcher.deleteAll(*article)
    }

    private fun updateAppLanguage(language: String, context: Context) {
        updatePreferredLanguage(language)
        changeLanguage(language, context)
        viewModelScope.launch {
            clearCachedArticles()
        }
    }

    private fun updatePreferredLanguage(language: String) {
        viewModelScope.launch {
            dataStoreRepository.updateLanguage(language.lowercase())
        }
    }

    private suspend fun clearCachedArticles() {
        val cachedArticles = cachedArticles.first()
        deleteAll(*cachedArticles.excludeSavedArticles().toTypedArray())
    }

    @Suppress("DEPRECATION")
    private fun changeLanguage(language: String, context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Use LocaleManager for Android 12 and later
            context.getSystemService(LocaleManager::class.java).applicationLocales = LocaleList.forLanguageTags(language)
        } else {
            // For earlier versions, update the app's configuration manually
            val locale = Locale(language)
            Locale.setDefault(locale)

            val resources = context.resources
            val configuration = resources.configuration
            configuration.setLocale(locale)
            resources.updateConfiguration(configuration, resources.displayMetrics)
        }
    }
}
