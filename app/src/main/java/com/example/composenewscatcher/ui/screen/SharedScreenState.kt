package com.example.composenewscatcher.ui.screen

import com.example.composenewscatcher.data.model.Article

data class SharedScreenState(
    val requestedArticle: Article? = null,
    val appLanguage: String? = null
)
