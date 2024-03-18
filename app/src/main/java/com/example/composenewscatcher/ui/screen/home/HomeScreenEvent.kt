package com.example.composenewscatcher.ui.screen.home

sealed interface HomeScreenEvent {
    data class UpdateSearchFieldInput(val input: String): HomeScreenEvent
    data class SearchForArticles(val searchQuery: String): HomeScreenEvent
}