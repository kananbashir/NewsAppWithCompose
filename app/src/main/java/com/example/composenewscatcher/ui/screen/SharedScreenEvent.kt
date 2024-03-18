package com.example.composenewscatcher.ui.screen

import com.example.composenewscatcher.data.model.Article

sealed interface SharedScreenEvent {
    data class UpdateRequestedArticle(val article: Article?): SharedScreenEvent
    data class UpdateArticleSavedStatus(val article: Article): SharedScreenEvent
    data class UpdateSharedLanguageState(val language: String): SharedScreenEvent
}