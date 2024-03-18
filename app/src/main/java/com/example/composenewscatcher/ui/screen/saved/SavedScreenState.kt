package com.example.composenewscatcher.ui.screen.saved

import com.example.composenewscatcher.data.model.Article

data class SavedScreenState(
    val savedArticles: List<Article> = listOf()
)
