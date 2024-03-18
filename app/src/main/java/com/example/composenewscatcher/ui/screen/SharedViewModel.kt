package com.example.composenewscatcher.ui.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Home
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composenewscatcher.data.model.Article
import com.example.composenewscatcher.data.repository.DataStoreRepository
import com.example.composenewscatcher.data.repository.NewsCatcherRepository
import com.example.composenewscatcher.ui.navigation.helper.NavigationBarItem
import com.example.composenewscatcher.util.constant.NavigationRouteConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val newsCatcherRepository: NewsCatcherRepository,
    private val dataStoreRepository: DataStoreRepository
): ViewModel() {

    private var languagePreference = dataStoreRepository.languagePreferenceFlow

    private var _navBarItems = listOf(
        NavigationBarItem(
            itemTitle = "Home",
            navRoute = NavigationRouteConstants.HOME_SCREEN,
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            hasNotification = false
        ),
        NavigationBarItem(
            itemTitle = "Saved",
            navRoute = NavigationRouteConstants.SAVED_ARTICLES_SCREEN,
            selectedIcon = Icons.Filled.Bookmark,
            unselectedIcon = Icons.Outlined.BookmarkBorder,
            hasNotification = false
        )
    )
    val navBarItems get() = _navBarItems

    private var _sharedScreenState = MutableStateFlow(SharedScreenState())
    val sharedScreenState = _sharedScreenState.asStateFlow()

    private var _currentRoute = MutableStateFlow(navBarItems.first().navRoute)
    val currentRoute = _currentRoute.asStateFlow()


    init {
        viewModelScope.launch {
            languagePreference.collect {
                _sharedScreenState.value = _sharedScreenState.value.copy(
                    appLanguage = it
                )
            }
        }
    }

    fun onSharedScreenEvent(sharedScreenEvent: SharedScreenEvent) = when (sharedScreenEvent) {
        is SharedScreenEvent.UpdateRequestedArticle -> updateRequestedArticleState(sharedScreenEvent.article)
        is SharedScreenEvent.UpdateArticleSavedStatus -> updateArticleSavedStatus( sharedScreenEvent.article)
        is SharedScreenEvent.UpdateSharedLanguageState -> updateSharedLanguageState(sharedScreenEvent.language)
    }

    private fun upsert(article: Article) = viewModelScope.launch {
        newsCatcherRepository.LocalDataSource().newsCatcher.upsert(article)
    }

    private fun updateArticleSavedStatus(article: Article) {
        val updatedArticle = article.copy(isSaved = !article.isSaved)
        upsert(updatedArticle)
        updateRequestedArticleState(updatedArticle)
    }

    private fun updateRequestedArticleState(article: Article?) {
        _sharedScreenState.value = _sharedScreenState.value.copy(
            requestedArticle = article?.copy()
        )
    }

    private fun updateSharedLanguageState(language: String) {
        _sharedScreenState.value = _sharedScreenState.value.copy(
            appLanguage = language
        )
    }

    fun updateCurrentRouteDestination(updatedRoute: String) {
        _currentRoute.value = updatedRoute
    }

}