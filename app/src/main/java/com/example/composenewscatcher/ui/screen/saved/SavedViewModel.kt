package com.example.composenewscatcher.ui.screen.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composenewscatcher.data.model.Article
import com.example.composenewscatcher.data.repository.NewsCatcherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedViewModel@Inject constructor(
    private val newsCatcherRepository: NewsCatcherRepository
): ViewModel() {

    private var _savedScreenState = MutableStateFlow(SavedScreenState())
    val savedScreenState = _savedScreenState.asStateFlow()

    private var savedArticles = newsCatcherRepository.LocalDataSource().newsCatcher.getAllSavedArticles()

    init {
        viewModelScope.launch {
            savedArticles.collect {
                updateSavedArticlesState(it)
            }
        }
    }

    private fun updateSavedArticlesState(savedArticles: List<Article>) {
        _savedScreenState.value = _savedScreenState.value.copy(
            savedArticles = savedArticles
        )
    }
}